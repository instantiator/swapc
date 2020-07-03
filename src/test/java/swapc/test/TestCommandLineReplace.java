package swapc.test;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.SystemOutRule;
import swapc.cmd.SwapC;
import swapc.lib.search.util.FileHelper;
import swapc.test.util.TestUtilities;

import java.io.File;

import static org.junit.Assert.assertTrue;

public class TestCommandLineReplace {

    @Rule
    public final SystemOutRule outRule = new SystemOutRule();

    @Before
    public void setUp() {
        outRule.clearLog();
        outRule.enableLog();
    }

    @Test
    public void testCanPlanCertificateReplacementsInYaml() throws Exception {
        File yamlCopy = FileHelper.generateNewTemporaryCopy(TestUtilities.getTestYamlFile());
        String yamlPath = yamlCopy.getPath();
        String replacementPath = TestUtilities.getExampleComCert().getPath();

        SwapC.main(new String[] { "replace", "-d", "-c=" + replacementPath, yamlPath });
        String log = outRule.getLog();
        assertTrue(log.contains("Recursion: disabled"));
        assertTrue(log.contains("Searching: " + yamlPath));
        assertTrue(log.contains("Examined 1 files."));
        assertTrue(log.contains("Found 4 certificates."));

        assertTrue(log.contains("Planned:  " + yamlCopy.getName() + ":.someCerts[0].aCertOnMultipleLines [B64,SP]"));
        assertTrue(log.contains("Planned:  " + yamlCopy.getName() + ":.someCerts[1].aCertOnOneLine [B64]"));
        assertTrue(log.contains("Planned:  " + yamlCopy.getName() + ":.someCerts[2].aCertOnMultipleLinesWithHeaders [B64,HDR,SP]"));
        assertTrue(log.contains("Planned:  " + yamlCopy.getName() + ":.someCerts[3].aCertOnMultipleLinesWithHeadersAndTwiceEncodedBackOntoOneLine [PEM,B64]"));

        assertTrue(log.contains("Temporary replacements made: 4"));
        assertTrue(log.contains("Permanent replacements made: 0"));

        yamlCopy.delete();
    }

    @Test
    public void testCanMakePermanentCertificateReplacementsInYaml() throws Exception {
        File yamlCopy = FileHelper.generateNewTemporaryCopy(TestUtilities.getTestYamlFile());
        String yamlPath = yamlCopy.getPath();
        String replacementPath = TestUtilities.getExampleComCert().getPath();

        SwapC.main(new String[] { "replace", "-c=" + replacementPath, yamlPath });
        String log = outRule.getLog();
        assertTrue(log.contains("Recursion: disabled"));
        assertTrue(log.contains("Searching: " + yamlPath));
        assertTrue(log.contains("Examined 1 files."));
        assertTrue(log.contains("Found 4 certificates."));

        assertTrue(log.contains("Planned:  " + yamlCopy.getName() + ":.someCerts[0].aCertOnMultipleLines [B64,SP]"));
        assertTrue(log.contains("Planned:  " + yamlCopy.getName() + ":.someCerts[1].aCertOnOneLine [B64]"));
        assertTrue(log.contains("Planned:  " + yamlCopy.getName() + ":.someCerts[2].aCertOnMultipleLinesWithHeaders [B64,HDR,SP]"));
        assertTrue(log.contains("Planned:  " + yamlCopy.getName() + ":.someCerts[3].aCertOnMultipleLinesWithHeadersAndTwiceEncodedBackOntoOneLine [PEM,B64]"));

        assertTrue(log.contains("Replaced: " + yamlCopy.getName() + ":.someCerts[0].aCertOnMultipleLines [B64,SP]"));
        assertTrue(log.contains("Replaced: " + yamlCopy.getName() + ":.someCerts[1].aCertOnOneLine [B64]"));
        assertTrue(log.contains("Replaced: " + yamlCopy.getName() + ":.someCerts[2].aCertOnMultipleLinesWithHeaders [B64,HDR,SP]"));
        assertTrue(log.contains("Replaced: " + yamlCopy.getName() + ":.someCerts[3].aCertOnMultipleLinesWithHeadersAndTwiceEncodedBackOntoOneLine [PEM,B64]"));

        assertTrue(log.contains("Temporary replacements made: 4"));
        assertTrue(log.contains("Permanent replacements made: 4"));

        yamlCopy.delete();
    }

}
