package swapc.lib.search.result;

import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;

public class CertificateFormat {

    public enum Encoding {
        Indecipherable,
        Binary,
        Base64
    }

    private final Encoding encoding;
    private final boolean hasPemHeaderAndFooter;
    private final String lineBreak;
    private final boolean isTwiceEncoded;

    private CertificateFormat(Encoding encoding, boolean pemHeaders, String lineBreak, boolean twiceEncoded) {
        this.encoding = encoding;
        this.hasPemHeaderAndFooter = pemHeaders;
        this.lineBreak = lineBreak;
        this.isTwiceEncoded = twiceEncoded;
    }

    public Encoding getEncoding() {
        return encoding;
    }

    public boolean hasPemHeaderAndFooter() {
        return hasPemHeaderAndFooter;
    }

    public String getLineBreak() {
        return lineBreak;
    }

    public boolean isTwiceEncoded() {
        return isTwiceEncoded;
    }

    public String describe() {
        List<String> attributes = new LinkedList<>();
        switch (encoding) {
            case Base64:
                if (hasPemHeaderAndFooter && "\n".equals(lineBreak)) {
                    attributes.add("PEM");
                } else {
                    attributes.add("B64");
                    if (hasPemHeaderAndFooter) { attributes.add("HDR"); }
                    if ("\n".equals(lineBreak)) { attributes.add("NL"); }
                    if (" ".equals(lineBreak)) { attributes.add("SP"); }
                }
                if (isTwiceEncoded) {
                    attributes.add("B64");
                }
                break;

            case Binary:
                attributes.add("DER");
                break;
        }

        return "[" + StringUtils.join(attributes, ",") + "]";
    }

    public static CertificateFormat pem(boolean pemHeaders, String lineBreak, boolean twiceEncoded) {
        return new CertificateFormat(Encoding.Base64, pemHeaders, lineBreak, twiceEncoded);
    }

    public static CertificateFormat der() {
        return new CertificateFormat(Encoding.Binary, false, null, false);
    }

    public static CertificateFormat indecipherable() {
        return new CertificateFormat(Encoding.Indecipherable, false, null, false);
    }
}
