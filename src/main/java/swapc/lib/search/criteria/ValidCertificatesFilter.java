package swapc.lib.search.criteria;

import java.security.cert.CertificateExpiredException;
import java.security.cert.CertificateNotYetValidException;
import java.security.cert.X509Certificate;
import java.util.Date;

public class ValidCertificatesFilter implements SearchCriteria.CertificateFilter {

    private final Date date;

    public ValidCertificatesFilter(Date date) {
        this.date = date;
    }

    @Override
    public boolean isMatch(X509Certificate certificate) {
        try {
            certificate.checkValidity(date);
            return true;
        } catch (CertificateNotYetValidException | CertificateExpiredException x) {
            return false;
        }
    }
}
