package com.prairiefarms.billing.utils;

import org.apache.commons.lang3.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class FolderMaintenance {

    public static void clean(final String pathAsString, final int days) throws Exception {
        final File directory = new File(pathAsString);
        final long purgeDate = System.currentTimeMillis() - ((long) days * 24 * 60 * 60 * 1000);

        if (directory.exists()) {
            final File[] files = directory.listFiles();

            if (ObjectUtils.isNotEmpty(files)) {
                for (File file : files) {
                    if (file.lastModified() <= purgeDate) {
                        if (!file.delete())
                            throw new Exception("Exception occurred while deleting file " + file.getPath());
                    }
                }
            }
        }
    }

    public static void move(final String from, final String to) throws IOException {
        //Files.move(Paths.get(from), Paths.get(to));
        Path sourcePath = Paths.get(from);
        Path targetPath = Paths.get(to);
        File file = targetPath.toFile();

        if (file.isFile()) Files.delete(targetPath);

        Files.move(sourcePath, targetPath);
    }
}
