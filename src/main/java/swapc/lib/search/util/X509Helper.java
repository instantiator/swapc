package swapc.lib.search.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.lang3.StringUtils;
import swapc.lib.search.result.CertificateFormat;
import swapc.lib.search.result.FoundCertificate;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import static swapc.lib.search.util.FileHelper.getExt;

public class X509Helper {

    private static final String CERT_BEGIN = "-----BEGIN CERTIFICATE-----";
    private static final String CERT_END = "-----END CERTIFICATE-----";

    public static FoundCertificate examineCertificateFile(File file) {
        if (file == null || !file.exists()) { return null; }

        String ext = getExt(file);
        if (ext == null || StringUtils.isEmpty(ext)) { return null; }

        try {
            if (isPemFileExt(ext)) {
                String b64 = Files.readString(file.toPath());
                return examinePem(b64);
            }

            if (isDerFileExt(ext)) {
                byte[] bytes = Files.readAllBytes(file.toPath());
                return examineDer(bytes);
            }

            return null;
        } catch (IOException e) {
            return null;
        }
    }

    public static FoundCertificate examinePem(String content) {
        return examinePem(content, false);
    }

    public static FoundCertificate examinePem(String content, boolean wasDecodedOnce) {
        boolean hasHeaders = content.trim().startsWith(CERT_BEGIN) && content.trim().endsWith(CERT_END);
        boolean hasNewLines = removeHeaders(content).contains("\n");
        boolean hasSpaces = removeHeaders(content).contains(" ");
        String guessLineSeparator = hasNewLines ? "\n" : hasSpaces ? " " : null;

        // attempt to load from PEM with assumptions above
        X509Certificate cert = fromPem(content);
        if (cert != null) {
            return new FoundCertificate(CertificateFormat.pem(hasHeaders, guessLineSeparator, wasDecodedOnce), cert);
        }

        // if no cert found and it has spaces, try it with new lines
        if (" ".equals(guessLineSeparator)) {
            String encodedWithNewLines = content.replace(" ", "\n");
            cert = fromPem(encodedWithNewLines);
            if (cert != null) {
                return new FoundCertificate(CertificateFormat.pem(hasHeaders, " ", wasDecodedOnce), cert);
            }
        }

        // if still no good, try breaking into lines of 64 chars (only if it has no headers)
        if (!hasHeaders && guessLineSeparator == null) {
            try {
                Base64 decoder = new Base64();
                Base64 encoder = new Base64(64);
                String encodedWithNewLines = encoder.encodeAsString(decoder.decode(content));
                cert = fromPem(encodedWithNewLines);
                if (cert != null) {
                    return new FoundCertificate(CertificateFormat.pem(hasHeaders, null, wasDecodedOnce), cert);
                }
            } catch (IllegalArgumentException e) {
                // NOP -- cannot reformat - this isn't base64
            }
        }

        // only attempt decoding from base64 if not tried before
        if (!wasDecodedOnce) {
            try {
                Base64 decoder = new Base64();
                String decoded = new String(decoder.decode(content));
                return examinePem(decoded, true);
            } catch (IllegalArgumentException e) {
                // NOP -- cannot reformat - this isn't base64
            }
        }

        return new FoundCertificate(CertificateFormat.indecipherable(), null);
    }

    public static String formatCertificate(X509Certificate certificate, boolean headers, String linebreak, boolean twice) throws CertificateEncodingException {
        byte[] bytes = certificate.getEncoded();
        Base64 encoder = linebreak == null ? new Base64(0) : new Base64(64, linebreak.getBytes());
        String base64 = encoder.encodeAsString(bytes);
        String cert64 = headers ? CERT_BEGIN + linebreak + base64 + linebreak + CERT_END : base64;
        if (twice) {
            Base64 encoder2 = new Base64(0);
            return encoder2.encodeAsString(cert64.getBytes());
        } else {
            return cert64;
        }
    }

    private static String removeHeaders(String b64) {
        if (b64.startsWith(CERT_BEGIN)) { b64 = b64.substring(CERT_BEGIN.length()).trim(); }
        if (b64.endsWith(CERT_END)) { b64 = b64.substring(0, b64.length()-CERT_END.length()).trim(); }
        return b64;
    }

    private static String applyHeaders(String b64) {
        if (!b64.startsWith(CERT_BEGIN)) {
            b64 = CERT_BEGIN + "\n" + b64;
        }
        if (!b64.endsWith("\n")) {
            b64 += "\n";
        }
        if (!b64.endsWith(CERT_END)) {
            b64 += CERT_END;
        }
        return b64;
    }

    private static X509Certificate fromPem(String b64) {
        try {
            // ensure PEM formatting headers and new lines are all in place...
            b64 = b64.trim();

            // remove header and footer
            b64 = removeHeaders(b64);

            // replace any spaces with new lines
            if (b64.contains(" ")) {
                b64 = b64.replace(" ", "\n");
            }

            // add certificate header and footer
            if (b64.contains("\n")) {
                b64 = applyHeaders(b64);
            }

            // generate certificate from PEM formatted b64
            CertificateFactory factory = CertificateFactory.getInstance("X.509");
            return (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(b64.getBytes()));
        }
        catch (IllegalArgumentException | CertificateException e) {
            return null;
        }
    }

    public static FoundCertificate examineDer(byte[] bytes) {
        try {
            CertificateFactory factory = CertificateFactory.getInstance("X.509");
            X509Certificate certificate = (X509Certificate) factory.generateCertificate(new ByteArrayInputStream(bytes));
            if (certificate != null) {
                return new FoundCertificate(CertificateFormat.der(), certificate);
            } else {
                return null;
            }
        } catch (CertificateException e) {
            return null;
        }
    }

    public static boolean isPemFileExt(String ext) { return ext.equals("pem") || ext.equals("b64"); }
    public static boolean isDerFileExt(String ext) { return ext.equals("cert") || ext.equals("crt") || ext.equals("x509") || ext.equals("der"); }

}
