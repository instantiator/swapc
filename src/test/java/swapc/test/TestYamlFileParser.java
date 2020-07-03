package swapc.test;

import org.junit.Test;
import swapc.lib.search.criteria.AcceptAnyCertificateFilter;
import swapc.lib.search.criteria.AcceptAnyFileFilter;
import swapc.lib.search.criteria.SearchCriteria;
import swapc.lib.search.parsers.YamlFileParser;
import swapc.lib.search.result.CertificateFormat;
import swapc.lib.search.result.CertificateMatch;
import swapc.test.util.TestUtilities;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class TestYamlFileParser {

    @Test
    public void testTestFilesAvailable() {
        File yamlFile = TestUtilities.getTestYamlFile();
        assertNotNull(yamlFile);
        assert (yamlFile.exists());
    }

    @Test
    public void testWillParseValidYaml() throws Exception {
        File file = TestUtilities.getTestYamlFile();
        YamlFileParser parser = new YamlFileParser();
        assert (parser.willAttempt(file));
        assert (parser.contentSafe(file));
    }

    @Test
    public void testCorrectlyIdentifiesCertificateFormatsInYaml() throws Exception {
        File file = TestUtilities.getTestYamlFile();
        YamlFileParser parser = new YamlFileParser();

        SearchCriteria criteria = new SearchCriteria(file, false);
        criteria.addFileFilters(new AcceptAnyFileFilter());
        criteria.addCertificateFilters(new AcceptAnyCertificateFilter());
        List<CertificateMatch> matches = parser.findCertificates(file, criteria);
        assertNotNull(matches);
        assertEquals(4, matches.size());

        CertificateMatch match;

        match = matches.get(0);
        assertEquals(file, match.getSourceFile());
        assertEquals(".someCerts[0].aCertOnMultipleLines", match.getInternalPath());
        assertEquals(CertificateFormat.Encoding.Base64, match.getFormat().getEncoding());
        assertEquals(CertificateMatch.FindLocationType.WithinFile, match.getLocationType());
        assertEquals(" ", match.getFormat().getLineBreak());
        assertFalse(match.getFormat().hasPemHeaderAndFooter());
        assertFalse(match.getFormat().isTwiceEncoded());
        assertNotNull(match.getCertificate());

        match = matches.get(1);
        assertEquals(file, match.getSourceFile());
        assertEquals(".someCerts[1].aCertOnOneLine", match.getInternalPath());
        assertEquals(CertificateFormat.Encoding.Base64, match.getFormat().getEncoding());
        assertEquals(CertificateMatch.FindLocationType.WithinFile, match.getLocationType());
        assertNull(match.getFormat().getLineBreak());
        assertFalse(match.getFormat().hasPemHeaderAndFooter());
        assertFalse(match.getFormat().isTwiceEncoded());
        assertNotNull(match.getCertificate());

        match = matches.get(2);
        assertEquals(file, match.getSourceFile());
        assertEquals(".someCerts[2].aCertOnMultipleLinesWithHeaders", match.getInternalPath());
        assertEquals(CertificateFormat.Encoding.Base64, match.getFormat().getEncoding());
        assertEquals(CertificateMatch.FindLocationType.WithinFile, match.getLocationType());
        assertEquals(" ", match.getFormat().getLineBreak()); // yaml doesn't preserve new lines
        assertTrue(match.getFormat().hasPemHeaderAndFooter());
        assertFalse(match.getFormat().isTwiceEncoded());
        assertNotNull(match.getCertificate());

        match = matches.get(3);
        assertEquals(file, match.getSourceFile());
        assertEquals(".someCerts[3].aCertOnMultipleLinesWithHeadersAndTwiceEncodedBackOntoOneLine", match.getInternalPath());
        assertEquals(CertificateFormat.Encoding.Base64, match.getFormat().getEncoding());
        assertEquals(CertificateMatch.FindLocationType.WithinFile, match.getLocationType());
        assertEquals("\n", match.getFormat().getLineBreak()); // b64 encoded new lines are preserved
        assertTrue(match.getFormat().hasPemHeaderAndFooter());
        assertTrue(match.getFormat().isTwiceEncoded());
        assertNotNull(match.getCertificate());
    }

}
