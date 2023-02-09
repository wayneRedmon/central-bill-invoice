package com.prairiefarms.billing.utils;

import com.prairiefarms.billing.BillingEnvironment;
import org.apache.commons.lang3.ObjectUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FolderMaintenance {

    private static final long PURGE_TIME = System.currentTimeMillis() - (30L * 24 * 60 * 60 * 1000);

    public FolderMaintenance() {
    }

    public void clean() throws Exception {
        final File directory = new File(BillingEnvironment.getInstance().emailSentBox());

        if (directory.exists()) {
            final File[] files = directory.listFiles();

            if (ObjectUtils.isNotEmpty(files)) {
                for (File file : files) {
                    if (file.lastModified() >= PURGE_TIME && !file.delete())
                        throw new Exception("Exception occurred while deleting file " + file.getPath());
                }
            }
        }
    }

    public void move(String from, String to) throws IOException {
        Files.move(Paths.get(from), Paths.get(to));
    }
}
