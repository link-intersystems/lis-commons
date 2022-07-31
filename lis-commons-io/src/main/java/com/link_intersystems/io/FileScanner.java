package com.link_intersystems.io;

import java.io.File;
import java.nio.file.FileSystem;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
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

    public FileScanner() {
    }

    /**
     * Copy constructor
     *
     * @param fileScanner
     */
    public FileScanner(FileScanner fileScanner) {
        this.includeDirPatterns = new ArrayList<>(fileScanner.includeDirPatterns);
        this.excludeDirPatterns = new ArrayList<>(fileScanner.excludeDirPatterns);
        this.includeFilePatterns = new ArrayList<>(fileScanner.includeFilePatterns);
        this.excludeFilePatterns = new ArrayList<>(fileScanner.excludeFilePatterns);
        this.fs = fileScanner.fs;
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

    public List<File> scan(Path basepath) {
        return scan(basepath.toFile());
    }

    public List<File> scan(File baseDir) {
        FileMatcher fileMatcher = createFileMatcher(baseDir.toPath());

        return scanDir(fileMatcher, baseDir);
    }

    private List<File> scanDir(FileMatcher fileMatcher, File dir) {
        List<File> fileMatches = new ArrayList<>();

        File[] files = dir.listFiles();
        if (files == null) {
            throw new IllegalArgumentException("The abstract pathname of '" + dir +
                    "' does not denote a directory, " +
                    "or an I/O error occurs.");
        }
        for (File file : files) {

            if (file.isFile() && fileMatcher.isFileMatch(file)) {
                fileMatches.add(file);
            } else if (file.isDirectory() && fileMatcher.isDirMatch(file)) {
                fileMatches.add(file);
            }

            if (file.isDirectory() && fileMatcher.processDirectory(file)) {
                List<File> matchingSubFiles = scanDir(fileMatcher, file);
                fileMatches.addAll(matchingSubFiles);
            }
        }

        return fileMatches;
    }


    private FileMatcher createFileMatcher(Path basepath) {
        List<PathMatcher> includeFilePathMatchers = toPathMatchers(includeFilePatterns);
        List<PathMatcher> excludeFilePathMatchers = toPathMatchers(excludeFilePatterns);
        List<PathMatcher> includeDirPathMatchers = toPathMatchers(includeDirPatterns);
        List<PathMatcher> excludeDirPathMatchers = toPathMatchers(excludeDirPatterns);

        return createFileMatcher(basepath, includeFilePathMatchers, excludeFilePathMatchers, includeDirPathMatchers, excludeDirPathMatchers);
    }

    protected FileMatcher createFileMatcher(Path basepath, List<PathMatcher> includeFilePathMatchers, List<PathMatcher> excludeFilePathMatchers, List<PathMatcher> includeDirPathMatchers, List<PathMatcher> excludeDirPathMatchers) {
        return new FileMatcher(basepath, includeFilePathMatchers, excludeFilePathMatchers, includeDirPathMatchers, excludeDirPathMatchers);
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

}
