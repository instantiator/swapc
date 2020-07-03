package swapc.lib.search.result;

import swapc.lib.search.criteria.SearchCriteria;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class MutableSearchResult implements SearchResult {

    private SearchCriteria criteria;
    private List<SwapCertException> exceptions;
    private List<CertificateMatch> matches;
    private List<File> files;

    public MutableSearchResult(SearchCriteria criteria) {
        this.criteria = criteria;
        this.exceptions = new LinkedList<>();
        this.matches = new LinkedList<>();
        this.files = new LinkedList<>();
    }

    @Override
    public SearchCriteria getSearchCriteria() {
        return criteria;
    }

    @Override
    public List<SwapCertException> getExceptions() {
        return exceptions;
    }

    @Override
    public List<CertificateMatch> getMatches() {
        return matches;
    }

    @Override
    public List<File> getSearchedFiles() {
        return files;
    }

    public void addException(SwapCertException exception) {
        this.exceptions.add(exception);
    }

    public void addMatches(List<CertificateMatch> matches) {
        this.matches.addAll(matches);
    }

    public void addSearchedFile(File file) {
        this.files.add(file);
    }

    public void merge(SearchResult result) {
        this.exceptions.addAll(result.getExceptions());
        this.matches.addAll(result.getMatches());
        this.files.addAll(result.getSearchedFiles());
    }
}
