package swapc.lib.search.result;

import java.util.LinkedList;
import java.util.List;

public class MutableReplaceResult implements ReplaceResult {

    private List<CertificateReplacement> replacements;
    private SearchResult searchResult;

    public MutableReplaceResult(SearchResult result) {
        this.searchResult = result;
        this.replacements = new LinkedList<>();
    }

    public void addReplacement(CertificateReplacement replacement) {
        replacements.add(replacement);
    }

    @Override
    public SearchResult getSearchResult() {
        return searchResult;
    }

    @Override
    public List<CertificateReplacement> getReplacements() {
        return replacements;
    }
}
