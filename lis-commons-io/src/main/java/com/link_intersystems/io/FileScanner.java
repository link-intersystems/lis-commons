package com.link_intersystems.io;

import java.io.File;
import java.net.URI;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Arrays.asList;
import static java.util.Objects.requireNonNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class FileScanner {

    private static class FileMatcher {

        private Path basepath;
        private List<PathMatcher> fileMatchers = new ArrayList<>();
        private List<PathMatcher> dirMatchers = new ArrayList<>();

        public FileMatcher(Path basepath, List<PathMatcher> fileMatchers, List<PathMatcher> dirMatchers) {
            this.basepath = basepath;
            this.fileMatchers.addAll(fileMatchers);
            this.dirMatchers.addAll(dirMatchers);
        }

        public Path getMatch(File file) {
            List<PathMatcher> pathMatchers;

            if (file.isDirectory()) {
                pathMatchers = dirMatchers;
            } else {
                pathMatchers = fileMatchers;
            }


            Path filepath = file.toPath();
            Path baseRelativePath = relativize(filepath, basepath);

            if (pathMatchers.stream().anyMatch(pm -> pm.matches(baseRelativePath))) {
                return baseRelativePath;
            }
            return null;
        }

        private Path relativize(Path filePath, Path basepath) {
            URI relativizedUri = basepath.toUri().relativize(filePath.toUri());
            return Paths.get(relativizedUri.getPath());
        }
    }

    private FileSystem fs = FileSystems.getDefault();

    private List<String> filePatterns = new ArrayList<>();
    private List<String> dirPatterns = new ArrayList<>();
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
        this.filePatterns.addAll(asList(globPatterns));
    }

    public void addDirectoryPatterns(String... globPatterns) {
        this.dirPatterns.addAll(asList(globPatterns));
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
                List<FilePath> subDirPaths = scanDir(fileMatcher, file);
                pathMatches.addAll(subDirPaths);
            }
        }

        return pathMatches;
    }


    private FileMatcher getFileMather() {
        List<PathMatcher> filePathMatchers = toPathMatchers(filePatterns);
        List<PathMatcher> dirPathMatchers = toPathMatchers(dirPatterns);
        return new FileMatcher(basepath, filePathMatchers, dirPathMatchers);
    }

    private List<PathMatcher> toPathMatchers(List<String> pattersn) {
        return pattersn.stream()
                .map(gp -> gp.startsWith("glob:") ? gp : "glob:" + gp)
                .map(fs::getPathMatcher)
                .collect(Collectors.toList());
    }
}
