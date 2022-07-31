package com.link_intersystems.io;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class FileMatcher {

    private Path basepath;
    private List<PathMatcher> includeFileMatchers;
    private List<PathMatcher> excludeFileMatchers;

    private List<PathMatcher> includeDirMatchers;
    private List<PathMatcher> excludeDirMatchers;

    public FileMatcher(Path basepath, List<PathMatcher> includeFileMatchers,
                       List<PathMatcher> excludeFileMatchers,
                       List<PathMatcher> includeDirMatchers,
                       List<PathMatcher> excludeDirMatchers) {
        this.basepath = basepath;
        this.includeFileMatchers = includeFileMatchers;
        this.excludeFileMatchers = excludeFileMatchers;
        this.includeDirMatchers = includeDirMatchers;
        this.excludeDirMatchers = excludeDirMatchers;
    }

    protected Path relativize(File file) {
        URI relativizedUri = basepath.toUri().relativize(file.toPath().toUri());
        return Paths.get(relativizedUri.getPath());
    }

    public boolean isFileMatch(File file) {
        
        Path basepathRelativePath = relativize(file);
        return includeFileMatchers.stream().anyMatch(pm -> pm.matches(basepathRelativePath)) &&
                excludeFileMatchers.stream().noneMatch(pm -> pm.matches(basepathRelativePath));
    }

    public boolean isDirMatch(File file) {
        Path basepathRelativePath = relativize(file);
        return includeDirMatchers.stream().anyMatch(pm -> pm.matches(basepathRelativePath)) &&
                excludeDirMatchers.stream().noneMatch(pm -> pm.matches(basepathRelativePath));
    }


    public boolean processDirectory(File file) {
        Path basepathRelativePath = relativize(file);
        if (includeDirMatchers.isEmpty()) {
            if (excludeDirMatchers.isEmpty()) {
                return true;
            }
        }
        return excludeDirMatchers.stream().noneMatch(pm -> pm.matches(basepathRelativePath));
    }
}
