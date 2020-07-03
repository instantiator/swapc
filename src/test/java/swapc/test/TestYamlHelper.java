package swapc.test;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;
import swapc.lib.search.criteria.AcceptAnyCertificateFilter;
import swapc.lib.search.criteria.SearchCriteria;
import swapc.lib.search.result.CertificateMatch;
import swapc.lib.search.result.SwapCertException;
import swapc.lib.search.util.YamlHelper;
import swapc.test.util.TestUtilities;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static swapc.lib.search.util.YamlHelper.findCertificatesInProperty;

public class TestYamlHelper {

    @SuppressWarnings("unchecked")
    @Test
    public void testCanReplaceAnEntryInYaml() throws Exception {

        File file = TestUtilities.getSimpleYamlFile();
        Yaml yaml = new Yaml();
        Map<String, Object> properties = (Map<String, Object>) yaml.load(new FileInputStream(file));

        boolean piggyOneOk = YamlHelper.replaceItem(properties, ".piggies.one", TestUtilities.SAMPLE_PUBLIC_UNCHAINED_CERT_concat);
        assertTrue(piggyOneOk);

        boolean numberIndex1Ok = YamlHelper.replaceItem(properties, ".numbers[1]", TestUtilities.SAMPLE_PUBLIC_UNCHAINED_CERT_spaces);
        assertTrue(numberIndex1Ok);

        boolean shoppingFood0Ok = YamlHelper.replaceItem(properties, ".shopping.food[0]", TestUtilities.SAMPLE_PUBLIC_UNCHAINED_CERT_concat);
        assertTrue(shoppingFood0Ok);

        boolean fluffyRollOk = YamlHelper.replaceItem(properties, ".shopping.other[1].toilet roll.fluffy", TestUtilities.SAMPLE_PUBLIC_UNCHAINED_CERT_spaces);
        assertTrue(fluffyRollOk);

        try {
            YamlHelper.replaceItem(properties, ".fairytales.cinderella.author", TestUtilities.SAMPLE_PUBLIC_UNCHAINED_CERT_concat);
            fail();
        } catch (SwapCertException e) {
            assertEquals(e.getCause().getMessage(), "Not found: .fairytales");
        }

        // now search the object structure for certificates
        SearchCriteria criteria = new SearchCriteria(file);
        criteria.addCertificateFilters(new AcceptAnyCertificateFilter());
        List<CertificateMatch> matches = findCertificatesInProperty(file, "", properties, criteria);

        assertEquals(4, matches.size());
        assertEquals(".piggies.one", matches.get(0).getInternalPath());
        assertEquals(".numbers[1]", matches.get(1).getInternalPath());
        assertEquals(".shopping.food[0]", matches.get(2).getInternalPath());
        assertEquals(".shopping.other[1].toilet roll.fluffy", matches.get(3).getInternalPath());
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testCanSaveNewYaml() throws Exception {
        File file = TestUtilities.getSimpleYamlFile();
        Yaml yaml = new Yaml();
        Map<String, Object> properties = (Map<String, Object>) yaml.load(new FileInputStream(file));

        boolean piggyOneOk = YamlHelper.replaceItem(properties, ".piggies.one", TestUtilities.SAMPLE_PUBLIC_UNCHAINED_CERT_concat);
        assertTrue(piggyOneOk);


    }
}