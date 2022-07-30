package com.link_intersystems.io;

import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.util.List;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class PathMatchers {

    private List<PathMatcher> includeFileMatchers;
    private List<PathMatcher> excludeFileMatchers;

    private List<PathMatcher> includeDirMatchers;
    private List<PathMatcher> excludeDirMatchers;

    public PathMatchers(List<PathMatcher> includeFileMatchers,
                        List<PathMatcher> excludeFileMatchers,
                        List<PathMatcher> includeDirMatchers,
                        List<PathMatcher> excludeDirMatchers) {
        this.includeFileMatchers = includeFileMatchers;
        this.excludeFileMatchers = excludeFileMatchers;
        this.includeDirMatchers = includeDirMatchers;
        this.excludeDirMatchers = excludeDirMatchers;
    }

    public boolean isDirMatch(Path path) {
        return includeDirMatchers.stream().anyMatch(pm -> pm.matches(path)) &&
                !excludeDirMatchers.stream().anyMatch(pm -> pm.matches(path));
    }

    public boolean isFileMatch(Path path) {
        return includeFileMatchers.stream().anyMatch(pm -> pm.matches(path)) &&
                !excludeFileMatchers.stream().anyMatch(pm -> pm.matches(path));
    }

    public boolean isProcessDir(Path path) {
        if (includeDirMatchers.isEmpty()) {
            if (excludeDirMatchers.isEmpty()) {
                return true;
            }
        }
        return !excludeDirMatchers.stream().anyMatch(pm -> pm.matches(path));
    }
}
