package swapc.lib.search.criteria;

import java.security.cert.X509Certificate;

public class AcceptAnyCertificateFilter implements SearchCriteria.CertificateFilter {

    @Override
    public boolean isMatch(X509Certificate certificate) {
        return certificate != null;
    }
}
