package swapc.lib.search.util;

import org.apache.commons.lang3.StringUtils;
import swapc.lib.search.criteria.SearchCriteria;
import swapc.lib.search.result.CertificateMatch;
import swapc.lib.search.result.FoundCertificate;
import swapc.lib.search.result.SwapCertException;
import swapc.lib.search.result.YamlFileCertMatch;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import static swapc.lib.search.util.ReflectionHelper.extendsClass;

public class YamlHelper {

    public static final String SEPARATOR = ".";
    public static final String SEPARATOR_regex = "\\.";

    @SuppressWarnings({"unchecked", "ConstantConditions"})
    public static List<CertificateMatch> findCertificatesInProperty(File file, String path, Object property, SearchCriteria criteria) {
        List<CertificateMatch> matches = new LinkedList<>();

        // Case: The property is a String. Check to see if it's a certificate, and list it if accepted by the criteria.
        if (property.getClass().equals(String.class)) {
            String value = (String)property;
            FoundCertificate found = X509Helper.examinePem(value);
            if (found.certificate != null && criteria.doesCertificateFilter(found.certificate)) {
                matches.add(new YamlFileCertMatch(file, path, found.format, found.certificate));
            }
        }

        // Case: The property is a Map. Step through each key in the map and parse what's inside.
        if (extendsClass(property.getClass(), Map.class)) { // actually a LinkedHashMap
            Map<String, Object> entries = (Map<String, Object>) property;
            for (String key : entries.keySet()) {
                matches.addAll(findCertificatesInProperty(file, path + SEPARATOR + key, entries.get(key), criteria));
            }
        }

        // Case: The property is a List. Step through each index in the list, and parse what's inside.
        if (extendsClass(property.getClass(), List.class)) { // actually ArrayList
            ArrayList<Object> list = (ArrayList<Object>) property;
            for (int i = 0; i < list.size(); i++) {
                matches.addAll(findCertificatesInProperty(file, path + "[" + i + "]", list.get(i), criteria));
            }
        }

        return matches;
    }

    public static boolean replaceItem(Map<String,Object> properties, String path, String replacement) throws SwapCertException {
        try {
            path = path.trim();
            if (path.startsWith(SEPARATOR)) { path = path.substring(SEPARATOR.length()); }
            if (StringUtils.isEmpty(path)) { return false; }
            String[] parts = path.split(SEPARATOR_regex);
            return replaceItem(properties, new LinkedList<>(), parts, replacement);

        } catch (IllegalArgumentException | IllegalStateException e) {
            throw new SwapCertException(e);
        }
    }

    @SuppressWarnings("unchecked")
    private static boolean replaceItem(Map<String,Object> properties, List<String> usedParts, String[] parts, String replacement) throws SwapCertException {
        if (parts.length < 1) { return false; }
        boolean replace = parts.length == 1;
        boolean follow = parts.length > 1;

        String keyPart = parts[0];
        String currentPath = String.join(SEPARATOR, usedParts) + SEPARATOR + keyPart;

        if (keyPart.contains("[")) {
            // this is an index into a list
            String[] keyAndIndex = keyPart.split("[\\[\\]]");
            Object property = properties.get(keyAndIndex[0]);

            if (extendsClass(property.getClass(), List.class)) {
                ArrayList<Object> list = (ArrayList<Object>) property;
                int index = Integer.parseInt(keyAndIndex[1]);
                if (list.size() > index) {
                    if (replace) {
                        list.set(index, replacement);
                        return true;
                    }
                    if (follow) {
                        Object followProperty = list.get(index);
                        if (followProperty == null) {
                            throw new IllegalArgumentException("Not found: " + currentPath);
                        }
                        if (extendsClass(followProperty.getClass(), Map.class)) {
                            String[] nextParts = (String[])Arrays.copyOfRange(parts, 1, parts.length);
                            usedParts.add(keyPart);
                            Map<String,Object> followMap = (Map<String,Object>)followProperty;
                            return replaceItem(followMap, usedParts, nextParts, replacement);
                        } else {
                            throw new SwapCertException("Cannot follow path, the next step is not a Map: " + currentPath);
                        }
                    }
                    throw new IllegalStateException("Neither replace nor follow encountered.");
                } else {
                    throw new SwapCertException("Index " + index + " not found at: " + currentPath);
                }
            } else {
                throw new SwapCertException("Not a list: " + currentPath);
            }

        } else {
            // we have a basic key
            if (replace) {
                properties.put(keyPart, replacement);
                return true;
            }
            if (follow) {
                Object followProperty = properties.get(keyPart);
                if (followProperty == null) {
                    throw new IllegalArgumentException("Not found: " + currentPath);
                }
                if (extendsClass(followProperty.getClass(), Map.class)) {
                    String[] nextParts = (String[])Arrays.copyOfRange(parts, 1, parts.length);
                    usedParts.add(keyPart);
                    Map<String,Object> followMap = (Map<String,Object>)followProperty;
                    return replaceItem(followMap, usedParts, nextParts, replacement);
                } else {
                    throw new SwapCertException("Cannot follow path, the next step is not a Map: " + currentPath);
                }
            }
            throw new IllegalStateException("Neither replace nor follow encountered.");
        }
    }

    public static boolean isYamlExt(String ext) {
        return ext.equals("yml") || ext.equals("yaml");
    }
}
