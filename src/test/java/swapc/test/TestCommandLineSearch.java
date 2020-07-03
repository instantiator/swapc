package swapc.test;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.ExpectedSystemExit;
import org.junit.contrib.java.lang.system.SystemOutRule;
import swapc.cmd.SwapC;
import swapc.lib.search.util.FileHelper;
import swapc.test.util.TestUtilities;

import java.io.File;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestCommandLineSearch {

    @Rule
    public final ExpectedSystemExit exit = ExpectedSystemExit.none();

    @Rule
    public final SystemOutRule outRule = new SystemOutRule();

    @Before
    public void setUp() {
        outRule.clearLog();
        outRule.enableLog();
    }

    @Test
    public void testNoParamsExits() {
        exit.expectSystemExit();
        SwapC.main(new String[0]);
    }

    @Test
    public void testCommandLineHelpShows() {
        SwapC.main(new String[] { "-h" });
        String log = outRule.getLog();
        assertTrue(log.contains("Show this help."));
    }

    @Test
    public void testSearchFindsOnly1ValidCerts() {
        String path = TestUtilities.getTestDataPath().getParentFile().getPath();
        SwapC.main(new String[] { "-r", "-f valid", "find", path });
        String log = outRule.getLog();
        assertTrue(log.contains("Applying filter: valid today"));
        assertTrue(log.contains("Searching: " + path));
        assertTrue(log.contains("Recursion: enabled"));
        assertTrue(log.contains("- example_com.cert [DER]"));
        assertTrue(log.contains("Found 1 certificates."));
    }

    @Test
    public void testSearchFindsAllCertsValidOn20121115() {
        String path = TestUtilities.getTestDataPath().getParentFile().getPath();
        SwapC.main(new String[] { "-r", "-f valid = 20121115", "find", path });
        String log = outRule.getLog();
        assertTrue(log.contains("Applying filter: valid on 20121115"));
        assertTrue(log.contains("Searching: " + path));
        assertTrue(log.contains("Recursion: enabled"));
        assertTrue(log.contains("Found 7 certificates."));
    }

    @Test
    public void testSearchFindsAllCertsInvalidToday() {
        String path = TestUtilities.getTestDataPath().getParentFile().getPath();
        SwapC.main(new String[] { "-r", "-f invalid", "find", path });
        String log = outRule.getLog();
        assertTrue(log.contains("Applying filter: invalid today"));
        assertTrue(log.contains("Searching: " + path));
        assertTrue(log.contains("Recursion: enabled"));
        assertTrue(log.contains("Found 7 certificates."));
    }

    @Test
    public void testSearchFindsAllCertsInvalidOn20200101() {
        String path = TestUtilities.getTestDataPath().getParentFile().getPath();
        SwapC.main(new String[] { "-r", "-f invalid = 20200101", "find", path });
        String log = outRule.getLog();
        assertTrue(log.contains("Applying filter: invalid on 20200101"));
        assertTrue(log.contains("Searching: " + path));
        assertTrue(log.contains("Recursion: enabled"));
        assertTrue(log.contains("Found 8 certificates."));
    }

    @Test
    public void testUnfilteredSearchFindsSampleYamlAndCertFiles() {
        String path = TestUtilities.getTestDataPath().getPath();

        SwapC.main(new String[] { "-r", "find", path });
        String log = outRule.getLog();
        assertTrue(log.contains("Searching: " + path));
        assertTrue(log.contains("Recursion: enabled"));

        assertTrue(log.contains("- sample_yaml_with_certs.yaml:.someCerts[0].aCertOnMultipleLines [B64,SP]"));
        assertTrue(log.contains("- sample_yaml_with_certs.yaml:.someCerts[1].aCertOnOneLine [B64]"));
        assertTrue(log.contains("- sample_yaml_with_certs.yaml:.someCerts[2].aCertOnMultipleLinesWithHeaders [B64,HDR,SP]"));
        assertTrue(log.contains("- sample_yaml_with_certs.yaml:.someCerts[3].aCertOnMultipleLinesWithHeadersAndTwiceEncodedBackOntoOneLine [PEM,B64]"));
        assertTrue(log.contains("- sample_public_unchained.b64 [B64,NL]"));
        assertTrue(log.contains("- sample_public_unchained.pem [PEM]"));
        assertTrue(log.contains("- sample_public_unchained.der [DER]"));
        assertTrue(log.contains("- example_com.cert [DER]"));
        assertTrue(log.contains("Found 8 certificates."));

        assertFalse(log.contains("- not_a_certificate.txt"));
        assertFalse(log.contains("- not_a_certificate.pem"));
        assertFalse(log.contains("- not_a_certificate.der"));
        assertFalse(log.contains("- not_a_yaml_file.yaml"));
    }

}
