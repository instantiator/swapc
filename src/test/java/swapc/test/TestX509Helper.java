package swapc.test;

import org.junit.Test;
import swapc.lib.search.result.CertificateFormat;
import swapc.lib.search.result.FoundCertificate;
import swapc.lib.search.util.X509Helper;
import swapc.test.util.TestUtilities;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class TestX509Helper {

    @Test
    public void testCanParseBase64Variants() {
        FoundCertificate found;

        found = X509Helper.examinePem(TestUtilities.SAMPLE_PUBLIC_UNCHAINED_CERT_concat);
        assertNotNull(found.certificate);
        assertEquals(CertificateFormat.Encoding.Base64, found.format.getEncoding());
        assertNull(found.format.getLineBreak());
        assertFalse(found.format.isTwiceEncoded());
        assertFalse(found.format.hasPemHeaderAndFooter());

        found = X509Helper.examinePem(TestUtilities.SAMPLE_PUBLIC_UNCHAINED_CERT_spaces);
        assertNotNull(found.certificate);
        assertEquals(CertificateFormat.Encoding.Base64, found.format.getEncoding());
        assertEquals(" ", found.format.getLineBreak());
        assertFalse(found.format.isTwiceEncoded());
        assertFalse(found.format.hasPemHeaderAndFooter());

        found = X509Helper.examinePem(TestUtilities.SAMPLE_PUBLIC_UNCHAINED_CERT_nl);
        assertNotNull(found.certificate);
        assertEquals(CertificateFormat.Encoding.Base64, found.format.getEncoding());
        assertEquals("\n", found.format.getLineBreak());
        assertFalse(found.format.isTwiceEncoded());
        assertFalse(found.format.hasPemHeaderAndFooter());
    }

}
