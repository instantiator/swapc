package swapc.cmd;

import picocli.CommandLine;
import swapc.lib.search.Replacer;
import swapc.lib.search.Searcher;
import swapc.lib.search.criteria.AcceptAnyCertificateFilter;
import swapc.lib.search.criteria.AcceptAnyFileFilter;
import swapc.lib.search.criteria.ExpiredCertificatesFilter;
import swapc.lib.search.criteria.SearchCriteria;
import swapc.lib.search.criteria.ValidCertificatesFilter;
import swapc.lib.search.result.CertificateMatch;
import swapc.lib.search.result.CertificateReplacement;
import swapc.lib.search.result.FoundCertificate;
import swapc.lib.search.result.ReplaceResult;
import swapc.lib.search.result.SearchResult;
import swapc.lib.search.result.SwapCertException;
import swapc.lib.search.util.DateTimeHelper;
import swapc.lib.search.util.X509Helper;

import java.io.File;
import java.security.cert.X509Certificate;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.Callable;

@CommandLine.Command(name = "swapc", description = "Search and replace certificates recursively by attributes.")
public class SwapC implements Callable<Integer> {

    private enum Verb { find, replace }

    @CommandLine.Parameters(index = "0", description = "Command: find / replace")
    private Verb verb;

    @CommandLine.Parameters(index = "1", description = "Path to search.")
    private File path;

    @CommandLine.Option(names = { "-h", "--help" }, usageHelp = true, description = "Show this help.")
    private boolean help = false;

    @CommandLine.Option(names = { "-r", "--recursive" }, description = "Search recursively. (Default disabled.)")
    boolean recursive = false;

    @CommandLine.Option(names = { "-f", "--filter" }, description = "Apply a filter, eg. \"invalid\" or \"invalid=YYYYMMDD\", or \"valid\", or \"valid=YYYYMMDD\"")
    List<String> filters = new LinkedList<>();

    @CommandLine.Option(names = { "-d", "--dry-run" }, description = "If set, will not apply temporary changes to permanent files. (Default disabled.)")
    private boolean dryRun;

    @CommandLine.Option(names = { "-c", "--replacement-cert" }, description = "The certificate to apply for replacement (required if 'replace' chosen).")
    private File replacementCertFile;

    @CommandLine.Option(names = { "-b", "--backup-files" }, description = "Set to make backup copies of files before changing them. (Default disabled.)")
    private boolean makeBackups;

    public static void main(String[] args) {
        int exitCode = new CommandLine(new SwapC()).execute(args);
        if (exitCode != 0) { System.exit(exitCode); }
    }

    @Override
    public Integer call() throws Exception {
        switch (verb) {
            case find:
                SearchResult searchResult = findCerts();
                printSearchResult(searchResult);
                return 0;

            case replace:
                SearchResult findResult = findCerts();
                printSearchResult(findResult);
                ReplaceResult replaceResult = replaceCerts(findResult);
                printReplaceResult(replaceResult);
                return 0;

            default:
                System.out.println("Unrecognised command.");
                return -1;
        }
    }

    private SearchResult findCerts() throws Exception {
        SearchCriteria criteria = new SearchCriteria(path, recursive);
        criteria.addCertificateFilters(new AcceptAnyCertificateFilter());
        criteria.addFileFilters(new AcceptAnyFileFilter());

        for (String filter : filters) {
            filter = filter.trim().toLowerCase();
            String[] parts = filter.split("=");

            if (parts[0].trim().equals("invalid")) {
                if (parts.length == 1) {
                    System.out.println("Applying filter: invalid today");
                    criteria.addCertificateFilters(new ExpiredCertificatesFilter(new Date()));
                } else {
                    Date date = DateTimeHelper.fromYYYYMMDD(parts[1].trim());
                    System.out.println("Applying filter: invalid on " + parts[1].trim());
                    criteria.addCertificateFilters(new ExpiredCertificatesFilter(date));
                }
            }
            if (parts[0].trim().equals("valid")) {
                if (parts.length == 1) {
                    System.out.println("Applying filter: valid today");
                    criteria.addCertificateFilters(new ValidCertificatesFilter(new Date()));
                } else {
                    Date date = DateTimeHelper.fromYYYYMMDD(parts[1].trim());
                    System.out.println("Applying filter: valid on " + parts[1].trim());
                    criteria.addCertificateFilters(new ValidCertificatesFilter(date));
                }
            }

        }

        System.out.println("Recursion: " + (recursive ? "enabled" : "disabled"));
        System.out.println("Searching: " + path.getPath());
        Searcher searcher = new Searcher();
        return searcher.search(criteria);
    }

    private ReplaceResult replaceCerts(SearchResult findResult) throws SwapCertException {
        X509Certificate certificate = getReplacementCertificate();
        Replacer replacer = new Replacer();
        ReplaceResult replaceResult = replacer.planReplacements(findResult, certificate);
        if (!dryRun) {
            replacer.makeReplacementsPermanent(replaceResult, makeBackups);
        }
        return replaceResult;
    }

    private X509Certificate getReplacementCertificate() {
        if (replacementCertFile != null && replacementCertFile.exists()) {
            FoundCertificate found = X509Helper.examineCertificateFile(replacementCertFile);
            if (found != null) {
                return found.certificate;
            } else {
                return null;
            }

        } else {
            throw new IllegalStateException("Please specify a valid certificate.");
        }
    }

    private void printSearchResult(SearchResult result) {
        for (CertificateMatch match : result.getMatches()) {
            System.out.println("- " + match.describeFullPath());
        }
        System.out.println("Examined " + result.getSearchedFiles().size() + " files.");
        System.out.println("Found " + result.getMatches().size() + " certificates.");
    }

    private void printReplaceResult(ReplaceResult result) {
        System.out.println();
        for (CertificateReplacement replacement : result.getReplacements()) {
            if (replacement.isMadeTemporary()) {
                System.out.println("Planned:  " + replacement.getMatch().describeFullPath());
            }
            if (replacement.isMadePermanent()) {
                System.out.println("Replaced: " + replacement.getMatch().describeFullPath());
            }
        }

        long temporaryReplacements = result.getReplacements().stream().filter(CertificateReplacement::isMadeTemporary).count();
        long permanentReplacements = result.getReplacements().stream().filter(CertificateReplacement::isMadePermanent).count();
        System.out.println("Temporary replacements made: " + temporaryReplacements);
        System.out.println("Permanent replacements made: " + permanentReplacements);
    }
}
