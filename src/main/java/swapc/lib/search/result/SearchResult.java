package swapc.lib.search.result;

import swapc.lib.search.criteria.SearchCriteria;

import java.io.File;
import java.util.List;

public interface SearchResult {

    SearchCriteria getSearchCriteria();

    List<SwapCertException> getExceptions();
    List<CertificateMatch> getMatches();
    List<File> getSearchedFiles();

}
