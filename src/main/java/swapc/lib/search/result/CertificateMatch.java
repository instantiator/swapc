package swapc.lib.search.result;

import java.io.File;
import java.security.cert.X509Certificate;

public interface CertificateMatch {

    enum FindLocationType { EntireFile, WithinFile }

    boolean isMatch();

    FindLocationType getLocationType();

    File getSourceFile();
    String getInternalPath();
    CertificateFormat getFormat();

    X509Certificate getCertificate();

    boolean replaceWith(X509Certificate cert, File workingCopy) throws SwapCertException;

    String describeFullPath();
}
