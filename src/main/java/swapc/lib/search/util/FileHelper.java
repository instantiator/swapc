package swapc.lib.search.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;

public class FileHelper {

    public static String getExt(File file) {
        String[] parts = file.getName().split("\\.");
        if (parts.length < 2) {
            return null;
        }
        return parts[parts.length - 1].toLowerCase();
    }

    public static File generateNewTemporaryCopy(File source) throws IOException {
        File temp = File.createTempFile("working-", "." + getExt(source));
        if (temp.exists()) { temp.delete(); }
        Files.copy(source.toPath(), temp.toPath());
        temp.deleteOnExit();
        return temp;
    }

    public static File generateNewTemporaryCopy(File source, Map<File,File> temporaryFiles) throws IOException {
        if (!source.exists()) { return null; }
        if (!temporaryFiles.containsKey(source)) {
            File temp = File.createTempFile("working-", "." + getExt(source));
            if (temp.exists()) { temp.delete(); }
            Files.copy(source.toPath(), temp.toPath());
            temporaryFiles.put(source, temp);
            temp.deleteOnExit();
        }
        return temporaryFiles.get(source);
    }

    public static File generateBackupCopy(File source) throws IOException {
        String backupFilename = source.getAbsolutePath() + ".backup";
        File backupFile = new File(backupFilename);
        if (backupFile.exists()) { backupFile.delete(); }
        Files.copy(source.toPath(), backupFile.toPath());
        return backupFile;
    }



}
