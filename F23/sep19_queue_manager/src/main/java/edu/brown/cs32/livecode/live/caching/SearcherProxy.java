package edu.brown.cs32.livecode.live.caching;

import java.util.List;

public class SearcherProxy implements Searcher {
    private final Searcher wrappedSearcher;

    // essentially does nothing
    SearcherProxy(Searcher realSearcher) {
        this.wrappedSearcher = realSearcher;
    }

    @Override
    // calls the wrapped object - foundation for more interesting things
    public List<String> searchLines(String target) {
        return this.wrappedSearcher.searchLines(target);
    }

    // could implement security checks, caching, change behavior of searcher
}
