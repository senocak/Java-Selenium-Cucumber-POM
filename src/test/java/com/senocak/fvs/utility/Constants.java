package com.senocak.fvs.utility;

import com.senocak.fvs.config.ConfigFileReader;
import com.senocak.fvs.config.User;
import lombok.extern.slf4j.Slf4j;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Slf4j
public class Constants {
    private static final ConfigFileReader configFileReader = ConfigFileReader.getInstance();

    public static final long EXPECTED_CONDITION_TIMEOUT = 30L;
    public static final long EXPECTED_CONDITION_POLLING_INTERVAL = 1L;
    public static final long DOCUMENT_READY_TIMEOUT = 60L;
    public static final long USER_WAIT_IN_MS = 1500L;
    public static final long WEBDRIVER_TIME_OUT_IN_SECONDS = 3;

    /**
     * Extract user from string
     * @param str string
     * @return user
     */
    public static User extractUserFromString(String str) {
        List<User> users = configFileReader.getUsers();
        if (str.startsWith("@user.")) {
            String extracted = str.substring(str.indexOf("[") + 1);
            extracted = extracted.substring(0, extracted.indexOf("]"));
            try {
                User user1 = users.get(Integer.parseInt(extracted) - 1);
                log.debug("Extracted user: {}", user1);
                return user1;
            } catch (IndexOutOfBoundsException | NumberFormatException e) {
                log.error("User with index {} is not defined in config file", extracted);
                throw new RuntimeException("User with index " + extracted + " is not defined in config file");
            }
        }
        return null;
    }

    public static void pack(final Path folder, final Path zipFilePath) throws IOException {
        try (
                FileOutputStream fos = new FileOutputStream(zipFilePath.toFile());
                ZipOutputStream zos = new ZipOutputStream(fos)
        ) {
            Files.walkFileTree(folder, new SimpleFileVisitor<Path>() {
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    zos.putNextEntry(new ZipEntry(folder.relativize(file).toString()));
                    Files.copy(file, zos);
                    zos.closeEntry();
                    return FileVisitResult.CONTINUE;
                }

                public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                    zos.putNextEntry(new ZipEntry(folder.relativize(dir).toString() + "/"));
                    zos.closeEntry();
                    return FileVisitResult.CONTINUE;
                }
            });
        }
    }
}
