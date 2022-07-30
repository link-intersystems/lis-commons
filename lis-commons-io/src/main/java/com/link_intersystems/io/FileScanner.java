package com.link_intersystems.io;

import java.io.File;
import java.net.URI;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class FileScanner {

    private FileSystem fs = FileSystems.getDefault();

    private List<String> includeFilePatterns = new ArrayList<>();
    private List<String> excludeFilePatterns = new ArrayList<>();
    private List<String> includeDirPatterns = new ArrayList<>();
    private List<String> excludeDirPatterns = new ArrayList<>();
    private Path basepath;

    public FileScanner(File basedir) {
        this(getBasepath(basedir));
    }

    private static Path getBasepath(File basedir) {
        if (basedir.isFile()) {
            throw new IllegalArgumentException("basedir must be a directory");
        }
        return basedir.toPath();
    }

    public Path getBasepath() {
        return basepath;
    }

    public FileScanner(Path basepath) {
        this.basepath = requireNonNull(basepath);
    }

    /**
     * Copy constructor
     *
     * @param fileScanner
     */
    public FileScanner(FileScanner fileScanner) {
        this.basepath = fileScanner.basepath;
        this.includeDirPatterns = new ArrayList<>(fileScanner.includeDirPatterns);
        this.excludeDirPatterns = new ArrayList<>(fileScanner.excludeDirPatterns);
        this.includeFilePatterns = new ArrayList<>(fileScanner.includeFilePatterns);
        this.excludeFilePatterns = new ArrayList<>(fileScanner.excludeFilePatterns);
    }

    public void setFileSystem(FileSystem fs) {
        this.fs = requireNonNull(fs);
    }

    public void addIncludeFilePattern(String... globPatterns) {
        this.includeFilePatterns.addAll(asList(globPatterns));
    }

    public void addExcludeFilePatterns(String... excludeGlobPatterns) {
        this.excludeFilePatterns.addAll(Arrays.asList(excludeGlobPatterns));
    }

    public void addIncludeDirectoryPatterns(String... globPatterns) {
        this.includeDirPatterns.addAll(asList(globPatterns));
    }

    public void addExcludeDirectoryPatterns(String... excludeGlobPatterns) {
        this.excludeDirPatterns.addAll(Arrays.asList(excludeGlobPatterns));
    }

    public List<FilePath> scan() {
        FileMatcher fileMatcher = createFileMatcher();

        File basefile = basepath.toFile();
        List<FilePath> filePaths = scanDir(fileMatcher, basefile);

        return filePaths;
    }

    private List<FilePath> scanDir(FileMatcher fileMatcher, File dir) {
        List<FilePath> pathMatches = new ArrayList<>();

        File[] files = dir.listFiles();
        for (File file : files) {
            Path filepath = file.toPath();
            Path baseRelativePath = relativize(filepath, basepath);

            if (file.isFile() && fileMatcher.isFileMatch(baseRelativePath)) {
                pathMatches.add(new FilePath(basepath, baseRelativePath));
            } else if (file.isDirectory() && fileMatcher.isDirMatch(baseRelativePath)) {
                pathMatches.add(new FilePath(basepath, baseRelativePath));
            }

            if (file.isDirectory() && fileMatcher.processDirectory(baseRelativePath)) {
                List<FilePath> subDirPaths = scanDir(fileMatcher, file);
                pathMatches.addAll(subDirPaths);
            }
        }

        return pathMatches;
    }

    /**
     * Makes the given path relative to the basepath, So that basepath.resolve(relativePath) would return the original path.
     */
    protected Path relativize(Path path, Path basepath) {
        URI relativizedUri = basepath.toUri().relativize(path.toUri());
        return Paths.get(relativizedUri.getPath());
    }

    private FileMatcher createFileMatcher() {
        List<PathMatcher> includeFilePathMatchers = toPathMatchers(includeFilePatterns);
        List<PathMatcher> excludeFilePathMatchers = toPathMatchers(excludeFilePatterns);
        List<PathMatcher> includeDirPathMatchers = toPathMatchers(includeDirPatterns);
        List<PathMatcher> excludeDirPathMatchers = toPathMatchers(excludeDirPatterns);

        return createFileMatcher(includeFilePathMatchers, excludeFilePathMatchers, includeDirPathMatchers, excludeDirPathMatchers);
    }

    protected FileMatcher createFileMatcher(List<PathMatcher> includeFilePathMatchers, List<PathMatcher> excludeFilePathMatchers, List<PathMatcher> includeDirPathMatchers, List<PathMatcher> excludeDirPathMatchers) {
        return new FileMatcher(includeFilePathMatchers, excludeFilePathMatchers, includeDirPathMatchers, excludeDirPathMatchers);
    }

    private List<PathMatcher> toPathMatchers(List<String> patterns) {
        return patterns.stream()
                .map(this::createPathMatcher)
                .collect(Collectors.toList());
    }

    protected PathMatcher createPathMatcher(String globPattern) {
        globPattern = globPattern.startsWith("glob:") ? globPattern : "glob:" + globPattern;
        return fs.getPathMatcher(globPattern);
    }

    public FileScanner rebase(Path newBasepath) {
        FileScanner copy = new FileScanner(this);
        copy.basepath = newBasepath;
        return copy;
    }
}
