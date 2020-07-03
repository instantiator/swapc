package swapc.lib.search;

import swapc.lib.search.result.CertificateMatch;
import swapc.lib.search.result.CertificateReplacement;
import swapc.lib.search.result.MutableCertificateReplacement;
import swapc.lib.search.result.MutableReplaceResult;
import swapc.lib.search.result.ReplaceResult;
import swapc.lib.search.result.SearchResult;
import swapc.lib.search.result.SwapCertException;

import java.io.File;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Replacer {

    public ReplaceResult planReplacements(SearchResult searchResult, X509Certificate certificate) throws SwapCertException {
        MutableReplaceResult result = new MutableReplaceResult(searchResult);
        Map<File, File> temporaryFiles = new HashMap<>();
        for (CertificateMatch match : searchResult.getMatches()) {
            CertificateReplacement replacement = new MutableCertificateReplacement(match, certificate);
            result.addReplacement(replacement);
            replacement.makeTemporary(temporaryFiles);
        }
        return result;
    }

    public void makeReplacementsPermanent(ReplaceResult result, boolean makeBackups) throws SwapCertException {
        List<File> replaced = new LinkedList<>(); // only replace each file once!
        for (CertificateReplacement replacement : result.getReplacements()) {
            if (replacement.isMadeTemporary() && !replacement.isMadePermanent()) {
                // candidate for making permanent
                if (!replaced.contains(replacement.getMatch().getSourceFile())) {
                    // file not yet made permanent
                    boolean succeeded = replacement.makePermanent(makeBackups);
                    if (succeeded) {
                        replaced.add(replacement.getPermanentReplacementFile());
                    }
                } else {
                    // file already made permanent - just mark the temp change
                    replacement.markPermanent();
                }
            }
        }
    }


}
