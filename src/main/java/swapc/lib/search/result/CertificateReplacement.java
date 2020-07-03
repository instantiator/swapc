package swapc.lib.search.result;

import swapc.cmd.SwapC;

import java.io.File;
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.Map;

public interface CertificateReplacement {
    CertificateMatch getMatch();
    X509Certificate getReplacement();

    boolean isMadeTemporary();
    boolean isMadePermanent();
    boolean isPermanentBackupAvailable();

    File getTemporaryReplacementFile();
    File getPermanentReplacementFile();
    File getPermanentBackupFile();

    boolean makeTemporary(Map<File,File> temporaryFiles) throws SwapCertException;
    boolean makePermanent(boolean backupPermanent) throws SwapCertException;
    void markPermanent();
}
