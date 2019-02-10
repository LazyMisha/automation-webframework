package com.project.utils;

import com.project.ddt.DataProviderManager;
import com.project.logger.Log;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import java.util.Properties;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class FileSystem {

    /**
     * this method return the Properties by name
     * @param fileName - name of properties file in resources
     * */
    public static Properties getProperties(String fileName) {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream(new File(Objects.requireNonNull(FileSystem.class.getClassLoader().getResource(fileName)).toURI())));
        } catch (IOException | URISyntaxException e) {
            Log.error("Can not load properties: '" + fileName + "'", e);
        }
        return properties;
    }

    /**
     * this method get the path to PageName.locators.properties file
     * */
    public static String getPathLocators() {
        StackTraceElement[] stackTraceElements;
        int count = (stackTraceElements = Thread.currentThread().getStackTrace()).length;
        String path = "";
        for (int i = 0; i < count; ++i) {
            StackTraceElement tmp = stackTraceElements[i];
            if (tmp.getClassName().contains("com.project.page.")) {
                String file = tmp.getFileName().replaceAll("\\..*", "");
                path = "/src/main/java/" + tmp.getClassName().replace(file, "")
                        .replaceAll("\\.", "/")
                        + "data/" + file + ".locators.properties";
                break;
            }
        }
        return path;
    }

    /**
     * return path to data file in '/ddt/data' package
     * @param dataFile - name of file to find
     * */
    public static String getDataProviderPath(String dataFile) {
        return getWorkingDir() + "/src/main/java/" +
                DataProviderManager.class.getPackage().getName().replace(".", "/") + "/data/" + dataFile + ".csv";
    }

    /**
     * this method create new .zip file and copy data from allure-results to the new .zip file
     * */
    static void zipReport() {
        String reportToZip = "allure-results";
        zipFile(new File(getWorkingDir() + File.separator + reportToZip));
        Log.info("Report '" + reportToZip + "' is copied to '" + getPathToZipReport() + "'");
    }

    /**
     * zip file in the project root directory
     * @param fileToZip - name of file to zip
     * */
    private static void zipFile(File fileToZip) {
        if (fileToZip.isHidden()) {
            Log.error("Report file '" + fileToZip.getName() + "' is hidden");
        }
        String zipFile = getWorkingDir() + File.separator + fileToZip.getName() + ".zip";
        Path zipPath = null;
        try {
            zipPath = Files.createFile(Paths.get(zipFile));
        } catch (IOException e) {
            Log.error("Zip file is nor created", e);
        }
        try (ZipOutputStream zipOutputStream = new ZipOutputStream(Files.newOutputStream(Objects.requireNonNull(zipPath)))) {
            Path fileToZipPath = Paths.get(fileToZip.getName());
            Files.walk(fileToZipPath)
                    .filter(path -> !Files.isDirectory(path))
                    .forEach(path -> {
                        ZipEntry zipEntry = new ZipEntry(fileToZipPath.relativize(path).toString());
                        try {
                            zipOutputStream.putNextEntry(zipEntry);
                            Files.copy(path, zipOutputStream);
                            zipOutputStream.closeEntry();
                        } catch (IOException e) {
                            Log.error("Can not copy report to zip file");
                        }
                    });
        } catch (IOException e) {
            Log.error("IOException", e);
        }
    }

    /**
     * this method delete file
     * @param fileName - name of file to delete
     * */
    public static void deleteFile(String fileName) {
        try {
            Files.delete(Paths.get(fileName));
            Log.info(fileName + " is deleted");
        } catch (IOException e) {
            Log.error("Can not delete file '" + fileName + "'", e);
        }
    }

    /**
     * after invoke method 'zipReport()' you can get path to zip report
     * */
    public static String getPathToZipReport() {
        return getWorkingDir() + File.separator + "allure-results.zip";
    }

    /**
     * return the current OS without digits. Just letters.
     * example - windows, linux
     * */
    public static String getCurrentOS() {
        return System.getProperty("os.name").replaceAll("[^a-zA-Z]", "").toLowerCase();
    }

    /**
     * return path to the project
     * */
    public static String getWorkingDir() {
        return System.getProperty("user.dir");
    }
}
