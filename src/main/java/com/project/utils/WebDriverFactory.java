package com.project.utils;

import com.project.logger.Log;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class WebDriverFactory {

    /**
     * initialize WebDriver depends on installed properties
     * @param webDriverProperties - set in '/resources/Run.config.properties' file
     * - browser
     * - run.type
     * - selenium.grid
     * */
    public static WebDriver initWebDriver(Map<String, String> webDriverProperties) {
        switch (webDriverProperties.get("browser")) {
            case "CHROME" :
                switch (webDriverProperties.get("run.type")) {
                    case "LOCAL":
                        Log.info("Initialize webdriver for local run");
                        setWebDriverChromeDriverToSystem();
                        return new ChromeDriver();
                    case "SELENIUM.GRID":
                        Log.info("Initialize webdriver using selenium.grid");
                        try {
                            return new RemoteWebDriver(new URL(webDriverProperties.get("selenium.grid")), new ChromeOptions());
                        } catch (UnreachableBrowserException e) {
                            Log.error("Remote server or browser start-up failure. Run grid server", e);
                        } catch (MalformedURLException e) {
                            Log.error("Can not initialize webDriver via 'selenium.grid' server: " + webDriverProperties.get("selenium.grid"), e);
                        }
                        break;
                }
            break;
        }
        return null;
    }

    /**
     * need to set up 'webdriver.chrome.driver' to system for 'LOCAL' run
     * */
    private static void setWebDriverChromeDriverToSystem() {
        String slash = File.separator;
        System.setProperty("webdriver.chrome.driver", FileSystem.getWorkingDir()
                + slash + "src" + slash + "main" + slash + "resources" +
                slash + FileSystem.getCurrentOS() + slash + "chromedriver" + (FileSystem.getCurrentOS().equals("windows") ? ".exe" : ""));
    }
}
