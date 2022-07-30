package com.link_intersystems.io;

import java.io.File;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

    public void setFileSystem(FileSystem fs) {
        this.fs = requireNonNull(fs);
    }

    public void addFilePattern(String... globPatterns) {
        this.includeFilePatterns.addAll(asList(globPatterns));
    }

    public void addExcludeFilePatterns(String... excludeGlobPatterns) {
        this.excludeFilePatterns.addAll(Arrays.asList(excludeGlobPatterns));
    }

    public void addDirectoryPatterns(String... globPatterns) {
        this.includeDirPatterns.addAll(asList(globPatterns));
    }

    public void addExcludeDirectoryPatterns(String... excludeGlobPatterns) {
        this.excludeDirPatterns.addAll(Arrays.asList(excludeGlobPatterns));
    }

    public List<FilePath> scan() {
        FileMatcher fileMatcher = getFileMather();

        File basefile = basepath.toFile();
        List<FilePath> filePaths = scanDir(fileMatcher, basefile);

        return filePaths;
    }


    private List<FilePath> scanDir(FileMatcher fileMatcher, File dir) {
        List<FilePath> pathMatches = new ArrayList<>();

        File[] files = dir.listFiles();
        for (File file : files) {
            Path match = fileMatcher.getMatch(file);
            if (match != null) {
                pathMatches.add(new FilePath(basepath, match));
            }

            if (file.isDirectory()) {
                if(fileMatcher.processDirectory(file)){
                    List<FilePath> subDirPaths = scanDir(fileMatcher, file);
                    pathMatches.addAll(subDirPaths);
                }
            }
        }

        return pathMatches;
    }


    private FileMatcher getFileMather() {
        PathMatchers pathMatchers = getPathMatchers();
        return new FileMatcher(basepath, pathMatchers);
    }

    private PathMatchers getPathMatchers(){
        List<PathMatcher> includeFilePathMatchers = toPathMatchers(includeFilePatterns);
        List<PathMatcher> excludeFilePathMatchers = toPathMatchers(excludeFilePatterns);
        List<PathMatcher> includeDirPathMatchers = toPathMatchers(includeDirPatterns);
        List<PathMatcher> excludeDirPathMatchers = toPathMatchers(excludeDirPatterns);

        return new PathMatchers(includeFilePathMatchers, excludeFilePathMatchers, includeDirPathMatchers, excludeDirPathMatchers);
    }

    private List<PathMatcher> toPathMatchers(List<String> pattersn) {
        return pattersn.stream()
                .map(gp -> gp.startsWith("glob:") ? gp : "glob:" + gp)
                .map(fs::getPathMatcher)
                .collect(Collectors.toList());
    }
}
