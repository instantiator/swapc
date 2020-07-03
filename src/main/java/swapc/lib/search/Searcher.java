package swapc.lib.search;

import swapc.lib.search.criteria.SearchCriteria;
import swapc.lib.search.parsers.CertificateFileParser;
import swapc.lib.search.parsers.FileParser;
import swapc.lib.search.parsers.YamlFileParser;
import swapc.lib.search.result.CertificateMatch;
import swapc.lib.search.result.MutableSearchResult;
import swapc.lib.search.result.SearchResult;
import swapc.lib.search.result.SwapCertException;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class Searcher {

    private List<FileParser> parsers;

    public Searcher() {
        this.parsers = createParsers();
    }

    private List<FileParser> createParsers() {
        List<FileParser> list = new LinkedList<>();
        list.add(new CertificateFileParser());
        list.add(new YamlFileParser());
        return list;
    }

    public SearchResult search(SearchCriteria criteria) {
        MutableSearchResult result = new MutableSearchResult(criteria);
        if (criteria.getSearchLocation().isDirectory()) {
            result.merge(searchDirectory(criteria.getSearchLocation(), criteria));
        } else {
            result.merge(examineFile(criteria.getSearchLocation(), criteria));
        }
        return result;
    }

    @SuppressWarnings("ConstantConditions")
    private SearchResult searchDirectory(File directory, SearchCriteria criteria) {
        MutableSearchResult result = new MutableSearchResult(criteria);
        for (File file : directory.listFiles()) {
            if (!shouldSkip(file)) {
                if (file.isDirectory() && criteria.isSearchRecursive()) {
                    result.merge(searchDirectory(file, criteria));
                }
                if (!file.isDirectory()) {
                    result.merge(examineFile(file, criteria));
                }
            }
        }
        return result;
    }

    private boolean shouldSkip(File file) {
        return file.getName().startsWith(".");
    }

    private MutableSearchResult examineFile(File file, SearchCriteria criteria) {
        MutableSearchResult result = new MutableSearchResult(criteria);
        if (!criteria.doesFileFilter(file)) { return result; }
        try {
            List<CertificateMatch> matches = findCertificates(file, criteria);
            result.addSearchedFile(criteria.getSearchLocation());
            result.addMatches(matches);
        } catch (Exception e) {
            result.addException(new SwapCertException(e));
        }
        return result;
    }

    private List<CertificateMatch> findCertificates(File file, SearchCriteria criteria) throws SwapCertException {
        List<CertificateMatch> matches = new LinkedList<>();
        for (FileParser parser : parsers) {
            if (parser.willAttempt(file)) {
                matches.addAll(parser.findCertificates(file, criteria));
            }
        }
        return matches;
    }

}
