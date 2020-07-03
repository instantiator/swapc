package swapc.lib.search.parsers;

import swapc.lib.search.criteria.SearchCriteria;
import swapc.lib.search.result.CertificateMatch;
import swapc.lib.search.result.SwapCertException;

import java.io.File;
import java.util.List;

public interface FileParser {

    boolean willAttempt(File file);
    boolean contentSafe(File file) throws SwapCertException;
    List<CertificateMatch> findCertificates(File file, SearchCriteria criteria) throws SwapCertException;

}
