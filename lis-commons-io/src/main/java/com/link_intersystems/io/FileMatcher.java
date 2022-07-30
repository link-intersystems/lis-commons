package com.link_intersystems.io;

import java.io.File;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
class FileMatcher {

    private Path basepath;
    private PathMatchers pathMatchers;

    public FileMatcher(Path basepath, PathMatchers pathMatchers) {
        this.basepath = requireNonNull(basepath);
        this.pathMatchers =  requireNonNull(pathMatchers);
    }

    public Path getMatch(File file) {

        Path filepath = file.toPath();
        Path baseRelativePath = relativize(filepath, basepath);


        if(file.isDirectory()){
            if(pathMatchers.isDirMatch(baseRelativePath)){
                return baseRelativePath;
            }
        } else if(file.isFile()){
            if(pathMatchers.isFileMatch(baseRelativePath)){
                return baseRelativePath;
            }
        }

        return null;
    }


    public boolean processDirectory(File dir) {

        Path filepath = dir.toPath();
        Path baseRelativePath = relativize(filepath, basepath);


        if(dir.isDirectory()){
            return pathMatchers.isProcessDir(baseRelativePath);
        }

        return false;
    }

    private Path relativize(Path filePath, Path basepath) {
        URI relativizedUri = basepath.toUri().relativize(filePath.toUri());
        return Paths.get(relativizedUri.getPath());
    }
}
