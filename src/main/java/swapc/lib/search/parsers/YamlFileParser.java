package swapc.lib.search.parsers;

import org.yaml.snakeyaml.Yaml;
import swapc.lib.search.criteria.SearchCriteria;
import swapc.lib.search.result.CertificateMatch;
import swapc.lib.search.result.SwapCertException;

import java.io.File;
import java.io.FileInputStream;
import java.util.List;
import java.util.Map;

import static swapc.lib.search.util.FileHelper.getExt;
import static swapc.lib.search.util.YamlHelper.findCertificatesInProperty;
import static swapc.lib.search.util.YamlHelper.isYamlExt;

public class YamlFileParser implements FileParser {

    private Yaml yaml;

    public YamlFileParser() {
        this.yaml = new Yaml();
    }

    @Override
    public boolean willAttempt(File file) {
        String ext = getExt(file);
        return ext != null && isYamlExt(ext);
    }

    @SuppressWarnings("unchecked")
    @Override
    public boolean contentSafe(File file) throws SwapCertException {
        try {
            Map<String, Object> properties = (Map<String, Object>) yaml.load(new FileInputStream(file));
            return properties != null && properties.size() > 0;
        } catch (Exception e) {
            throw new SwapCertException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<CertificateMatch> findCertificates(File file, SearchCriteria criteria) throws SwapCertException {
        try {
            Map<String, Object> properties = (Map<String, Object>) yaml.load(new FileInputStream(file));
            return findCertificatesInProperty(file, "", properties, criteria);
        } catch (Exception e) {
            throw new SwapCertException(e);
        }
    }

}
