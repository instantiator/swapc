package swapc.lib.search.result;

import java.security.cert.X509Certificate;

public class FoundCertificate {
    public final CertificateFormat format;
    public final X509Certificate certificate;

    public FoundCertificate(CertificateFormat format, X509Certificate certificate) {
        this.format = format;
        this.certificate = certificate;
    }
}
