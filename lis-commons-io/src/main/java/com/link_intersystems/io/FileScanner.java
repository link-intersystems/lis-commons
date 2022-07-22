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

    private FileSystem fs = FileSystems.getDefault();

    private List<String> globPatterns = new ArrayList<>();

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

    public FileScanner(Path basepath) {
        this.basepath = requireNonNull(basepath);
    }

    public void setFileSystem(FileSystem fs) {
        this.fs = requireNonNull(fs);
    }

    public void addGlobPattern(String... globPatterns) {
        this.globPatterns.addAll(asList(globPatterns));
    }

    public List<Path> scan() {
        List<PathMatcher> pathMatchers = getPathMatchers();

        File basefile = basepath.toFile();
        List<Path> matchedPaths = scanDir(pathMatchers, basefile);

        return matchedPaths;
    }

    private List<Path> scanDir(List<PathMatcher> pathMatchers, File dir) {
        List<Path> matchedPaths = new ArrayList<>();

        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                List<Path> subDirPaths = scanDir(pathMatchers, file);
                matchedPaths.addAll(subDirPaths);
            } else {
                Path filePath = file.toPath();
                Path baseRelativePath = relativize(filePath, basepath);

                if (pathMatchers.stream().anyMatch(pm -> pm.matches(baseRelativePath))) {
                    matchedPaths.add(baseRelativePath);
                }
            }
        }

        return matchedPaths;
    }

    private Path relativize(Path filePath, Path basepath) {
        URI relativizedUri = basepath.toUri().relativize(filePath.toUri());
        return Paths.get(relativizedUri.getPath());
    }

    private List<PathMatcher> getPathMatchers() {
        return
                globPatterns.stream()
                        .map(gp -> gp.startsWith("glob:") ? gp : "glob:" + gp)
                        .map(fs::getPathMatcher)
                        .collect(Collectors.toList());
    }

}
