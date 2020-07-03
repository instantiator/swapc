package swapc.lib.search.criteria;

import java.io.File;

public class AcceptAnyFileFilter implements SearchCriteria.FileFilter {

    @Override
    public boolean isMatch(File file) {
        return file != null && file.exists();
    }

}
