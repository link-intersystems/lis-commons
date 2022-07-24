package com.link_intersystems.io;

import java.nio.file.Path;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.List;

/**
 * @author René Link {@literal <rene.link@link-intersystems.com>}
 */
public class PathMatches extends AbstractList<PathMatch> {

    List<PathMatch> pathMatches = new ArrayList<>();

    public PathMatches(List<PathMatch> pathMatches) {
        this.pathMatches.addAll(pathMatches);
    }

    @Override
    public PathMatch get(int index) {
        return pathMatches.get(index);
    }

    @Override
    public int size() {
        return pathMatches.size();
    }

    public boolean containsMatch(Path path) {
        return stream().anyMatch(pm -> pm.getPath().equals(path));
    }
}
