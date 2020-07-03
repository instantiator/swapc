package swapc.test;

import org.junit.Test;
import swapc.lib.search.criteria.AcceptAnyCertificateFilter;
import swapc.lib.search.criteria.AcceptAnyFileFilter;
import swapc.lib.search.criteria.SearchCriteria;
import swapc.lib.search.parsers.CertificateFileParser;
import swapc.lib.search.result.CertificateFormat;
import swapc.lib.search.result.CertificateMatch;
import swapc.test.util.TestUtilities;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class TestCertificateFileParser {

    @Test
    public void testTestFilesAvailable() {
        File certFile = TestUtilities.getTestB64File();
        assertNotNull(certFile);
        assert(certFile.exists());
    }

    @Test
    public void testWillParseTestPem() throws Exception {
        File file = TestUtilities.getTestB64File();
        CertificateFileParser parser = new CertificateFileParser();
        assert(parser.willAttempt(file));
        assert(parser.contentSafe(file));
    }

    @Test
    public void testCorrcetlyIdentifiesDetailsFromTestPem() throws Exception {
        File file = TestUtilities.getTestB64File();
        CertificateFileParser parser = new CertificateFileParser();

        SearchCriteria criteria = new SearchCriteria(file, false);
        criteria.addFileFilters(new AcceptAnyFileFilter());
        criteria.addCertificateFilters(new AcceptAnyCertificateFilter());
        List<CertificateMatch> matches = parser.findCertificates(file, criteria);
        assertEquals(1, matches.size());
        assertEquals(CertificateFormat.Encoding.Base64, matches.get(0).getFormat().getEncoding());
        assertEquals(matches.get(0).getFormat().getLineBreak(), "\n");
        assertFalse(matches.get(0).getFormat().hasPemHeaderAndFooter());
        assertFalse(matches.get(0).getFormat().isTwiceEncoded());
        assertNotNull(matches.get(0).getCertificate());
        assertEquals("O=Internet Widgits Pty Ltd, ST=Some-State, C=AU", matches.get(0).getCertificate().getSubjectDN().getName());
    }

}
