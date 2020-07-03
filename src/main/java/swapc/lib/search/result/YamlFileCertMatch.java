package swapc.lib.search.result;

import org.yaml.snakeyaml.Yaml;
import swapc.lib.search.util.X509Helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Map;

import static swapc.lib.search.util.YamlHelper.replaceItem;

public class YamlFileCertMatch implements CertificateMatch {

    private File sourceFile;
    private CertificateFormat format;
    private X509Certificate certificate;
    private String internalPath;

    public YamlFileCertMatch(File sourceFile, String yamlPath, CertificateFormat format, X509Certificate cert) {
        this.certificate = cert;
        this.format = format;
        this.sourceFile = sourceFile;
        this.internalPath = yamlPath;
    }

    @Override
    public boolean isMatch() {
        return certificate != null;
    }

    @Override
    public FindLocationType getLocationType() {
        return FindLocationType.WithinFile;
    }

    @Override
    public File getSourceFile() {
        return sourceFile;
    }

    @Override
    public String getInternalPath() {
        return internalPath;
    }

    @Override
    public CertificateFormat getFormat() {
        return format;
    }

    @Override
    public X509Certificate getCertificate() {
        return certificate;
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean replaceWith(X509Certificate certificate, File workingFile) throws SwapCertException {
        try {
            switch (format.getEncoding()) {
                case Base64:
                    String linebreak = format.getLineBreak();
                    boolean headers = format.hasPemHeaderAndFooter();
                    boolean twice = format.isTwiceEncoded();
                    String newCertificateFormatted = X509Helper.formatCertificate(certificate, headers, linebreak, twice);

                    Yaml yaml = new Yaml();
                    Map<String, Object> properties = (Map<String, Object>) yaml.load(new FileInputStream(workingFile));

                    // TODO: locate the item, replace with newCertificateFormatted
                    boolean ok = replaceItem(properties, internalPath, newCertificateFormatted);
                    if (!ok) {
                        throw new SwapCertException("Could not replace value at path: " + internalPath);
                    }

                    String newYaml = yaml.dump(properties);
                    Files.writeString(workingFile.toPath(), newYaml); // no options implies WRITE, CREATE, TRUNCATE_EXISTING
                    return true;

                case Binary:
                    throw new IllegalArgumentException("Unable to embed a binary certificate in a YAML file.");

                default:
                    throw new IllegalArgumentException("Unrecognised certificate format.");
            }
        } catch (IllegalArgumentException | CertificateEncodingException | IOException e) {
            throw new SwapCertException("Could not encode the new certificate or replace the working file.", e);
        }
    }

    @Override
    public String describeFullPath() {
        String fullPath = sourceFile.getName();
        if (internalPath != null) { fullPath = fullPath + ":" + internalPath; }
        return fullPath + " " + format.describe();
    }
}
