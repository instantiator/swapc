package swapc.lib.search.result;

import swapc.cmd.SwapC;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.cert.X509Certificate;
import java.util.Map;

import static swapc.lib.search.util.FileHelper.generateBackupCopy;
import static swapc.lib.search.util.FileHelper.generateNewTemporaryCopy;

public class MutableCertificateReplacement implements CertificateReplacement {

    private CertificateMatch match;
    private X509Certificate replacement;
    File temporaryReplacementFile;
    File permanentReplacementFile;
    File permanentBackupFile;

    public MutableCertificateReplacement(CertificateMatch match, X509Certificate replacement) {
        this.match = match;
        this.replacement = replacement;
    }

    @Override
    public CertificateMatch getMatch() {
        return match;
    }

    @Override
    public X509Certificate getReplacement() {
        return replacement;
    }

    @Override
    public boolean isMadeTemporary() {
        return temporaryReplacementFile != null && temporaryReplacementFile.exists();
    }

    @Override
    public boolean isMadePermanent() {
        return permanentReplacementFile != null && permanentReplacementFile.exists();
    }

    @Override
    public boolean isPermanentBackupAvailable() {
        return permanentBackupFile != null;
    }

    @Override
    public File getTemporaryReplacementFile() {
        return temporaryReplacementFile;
    }

    @Override
    public File getPermanentReplacementFile() {
        return permanentReplacementFile;
    }

    @Override
    public File getPermanentBackupFile() {
        return permanentBackupFile;
    }

    @Override
    public boolean makeTemporary(Map<File, File> temporaryFiles) throws SwapCertException {
        try {
            // initialise a temporary working copy of the file
            temporaryReplacementFile = generateNewTemporaryCopy(match.getSourceFile(), temporaryFiles);

            // invoke the change
            boolean ok = match.replaceWith(replacement, temporaryReplacementFile);

            // ensure that the temporary file doesn't hang around
            if (ok) {
                temporaryReplacementFile.deleteOnExit();
            } else {
                temporaryReplacementFile.delete();
                temporaryReplacementFile = null;
            }
            return ok;

        } catch (IOException e) {
            throw new SwapCertException(e);
        }
    }

    @Override
    public void markPermanent() {
        permanentReplacementFile = match.getSourceFile();
    }

    @Override
    public boolean makePermanent(boolean backupPermanent) throws SwapCertException {
        if (!temporaryReplacementFile.exists()) { throw new SwapCertException("No temporary replacement to make permanent."); }

        try {
            File backup = generateBackupCopy(match.getSourceFile());
            if (!backup.exists()) { throw new SwapCertException("Cannot safely make replacement permanent, backup not created."); }

            if (match.getSourceFile().exists()) { match.getSourceFile().delete(); }

            Files.copy(temporaryReplacementFile.toPath(), match.getSourceFile().toPath());
            if (match.getSourceFile().exists()) {
                // success - record as permanent
                permanentReplacementFile = match.getSourceFile();
            } else {
                // failed - so restore the backup
                Files.copy(backup.toPath(), permanentReplacementFile.toPath());
            }

            if (!backupPermanent) {
                backup.delete();
            }

            return true;

        } catch (IOException e) {
            throw new SwapCertException(e);
        }
    }
}
