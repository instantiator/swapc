package swapc.lib.search.result;

import swapc.lib.search.util.X509Helper;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;

public class CertificateFileMatch implements CertificateMatch {

    private File sourceFile;
    private CertificateFormat format;
    private X509Certificate certificate;

    public CertificateFileMatch(File sourceFile, CertificateFormat format, X509Certificate cert) {
        this.certificate = cert;
        this.format = format;
        this.sourceFile = sourceFile;
    }

    @Override
    public boolean isMatch() {
        return certificate != null;
    }

    @Override
    public FindLocationType getLocationType() {
        return FindLocationType.EntireFile;
    }

    @Override
    public File getSourceFile() {
        return sourceFile;
    }

    @Override
    public String getInternalPath() {
        return null; // not relevant to certificate files
    }

    @Override
    public CertificateFormat getFormat() {
        return format;
    }

    @Override
    public X509Certificate getCertificate() {
        return certificate;
    }

    @Override
    public boolean replaceWith(X509Certificate certificate, File workingFile) throws SwapCertException {
        try {
            switch (format.getEncoding()) {
                case Base64:
                    String linebreak = format.getLineBreak();
                    boolean headers = format.hasPemHeaderAndFooter();
                    boolean twice = format.isTwiceEncoded();
                    String newCertificateFormatted = X509Helper.formatCertificate(certificate, headers, linebreak, twice);
                    Files.writeString(workingFile.toPath(), newCertificateFormatted); // no options implies WRITE, CREATE, TRUNCATE_EXISTING
                    return true;

                case Binary:
                    byte[] newCertificateBytes = certificate.getEncoded();
                    Files.write(workingFile.toPath(), newCertificateBytes); // no options implies WRITE, CREATE, TRUNCATE_EXISTING
                    return true;

                default:
                    throw new IllegalArgumentException("Unrecognised certificate format.");
            }
        } catch (IllegalArgumentException | CertificateEncodingException | IOException e) {
            throw new SwapCertException("Could not encode the new certificate or replace the working file.", e);
        }
    }

    @Override
    public String describeFullPath() {
        return sourceFile.getName() + " " + format.describe();
    }
}
