package swapc.lib.search.criteria;

import java.io.File;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class SearchCriteria {

    private final File location;
    private final boolean recursive;
    private final List<FileFilter> fileFilters;
    private final List<CertificateFilter> certificateFilters;

    public SearchCriteria(File singleFile) {
        this.location = singleFile;
        this.recursive = false;
        this.fileFilters = new LinkedList<>();
        this.certificateFilters = new LinkedList<>();
    }

    public SearchCriteria(File directory, boolean recursive) {
        this.location = directory;
        this.recursive = recursive;
        this.fileFilters = new LinkedList<>();
        this.certificateFilters = new LinkedList<>();
    }

    public File getSearchLocation() {
        return location;
    }

    public boolean isSearchRecursive() {
        return recursive;
    }

    public void addFileFilters(FileFilter... filters) {
        fileFilters.addAll(Arrays.asList(filters));
    }

    public void addFileFilters(List<FileFilter> filters) {
        fileFilters.addAll(filters);
    }

    public void addCertificateFilters(List<CertificateFilter> filters) {
        certificateFilters.addAll(filters);
    }

    public void addCertificateFilters(CertificateFilter... filters) {
        certificateFilters.addAll(Arrays.asList(filters));
    }

    public boolean doesFileFilter(File file) {
        return
            getFileFilters().size() == 0 ||
            getFileFilters().stream().allMatch(filter -> filter.isMatch(file));
    }

    public List<FileFilter> getFileFilters() {
        return fileFilters;
    }

    public List<CertificateFilter> getCertificateFilters() {
        return certificateFilters;
    }

    public boolean doesCertificateFilter(X509Certificate certificate) {
        return
            getCertificateFilters().size() == 0 ||
            getCertificateFilters().stream().allMatch(filter -> filter.isMatch(certificate));
    }

    public static interface CertificateFilter {

        boolean isMatch(X509Certificate certificate);

    }

    public static interface FileFilter {

        boolean isMatch(File file);

    }
}
