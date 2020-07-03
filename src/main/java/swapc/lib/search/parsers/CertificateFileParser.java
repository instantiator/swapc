package swapc.lib.search.parsers;

import swapc.lib.search.criteria.SearchCriteria;
import swapc.lib.search.result.CertificateFileMatch;
import swapc.lib.search.result.CertificateFormat;
import swapc.lib.search.result.CertificateMatch;
import swapc.lib.search.result.FoundCertificate;
import swapc.lib.search.result.SwapCertException;

import java.io.File;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

import static swapc.lib.search.util.FileHelper.getExt;
import static swapc.lib.search.util.X509Helper.examineCertificateFile;
import static swapc.lib.search.util.X509Helper.examinePem;
import static swapc.lib.search.util.X509Helper.isDerFileExt;
import static swapc.lib.search.util.X509Helper.isPemFileExt;

public class CertificateFileParser implements FileParser {

    @Override
    public boolean willAttempt(File file) {
        String ext = getExt(file);
        return ext != null && (isPemFileExt(ext) || isDerFileExt(ext));
    }

    @Override
    public boolean contentSafe(File file) throws SwapCertException {
        try {
            String b64 = Files.readString(file.toPath());
            FoundCertificate found = examinePem(b64);
            return found.certificate != null;
        } catch (Exception e) {
            throw new SwapCertException(e);
        }
    }

    @Override
    public List<CertificateMatch> findCertificates(File file, SearchCriteria criteria) throws SwapCertException {
        List<CertificateMatch> matches = new LinkedList<>();
        try {
            FoundCertificate found = examineCertificateFile(file);
            if (found.format.getEncoding() != CertificateFormat.Encoding.Indecipherable &&
                found.certificate != null &&
                criteria.doesCertificateFilter(found.certificate)) {
                matches.add(new CertificateFileMatch(file, found.format, found.certificate));
            }

        } catch (Exception e) {
            throw new SwapCertException(e);
        }

        return matches;
    }

}
