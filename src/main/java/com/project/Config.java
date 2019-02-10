package com.project;

import com.project.logger.Log;
import com.project.page.PagesContainer;
import com.project.utils.WebDriverFactory;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import static com.project.utils.FileSystem.getProperties;

public class Config {

    private Properties properties;

    private static WebDriver webDriver;
    protected PagesContainer web;
    private String seleniumGridServer;
    private String browser;
    private String runType;

    @BeforeMethod
    public void setUp() {
        Log.info("Start set up");
        if (webDriver == null) {
            webDriver = WebDriverFactory.initWebDriver(getWebDriverProperties());
        }

        webDriver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
        webDriver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
        webDriver.manage().timeouts().setScriptTimeout(60, TimeUnit.SECONDS);

        web = new PagesContainer();
    }

    public static WebDriver getWebDriver() {
        return webDriver;
    }

    /**
     * overloaded open(String, boolean) method with 'clearCookie = true' by default
     * @param URL - can be name of URL from 'resources/EntryPoints.properties' or URL
     * */
    protected void open(String URL){
        open(URL, true);
    }

    /**
     * open site
     * @param URL - can be name of URL from 'resources/EntryPoints.properties' or URL
     * @param clearCookie - if 'true' that cookie will be cleared before open URL
     * */
    public void open(String URL, boolean clearCookie){
        if (clearCookie) {
            webDriver.manage().deleteAllCookies();
            Log.info("Clear cookies");
        }

        if (!(URL.startsWith("https://")) && !(URL.startsWith("http://"))) {
            properties = getProperties("EntryPoints.properties");
            Log.info("Entry point name: '" + URL + "'");
            URL = properties.getProperty(URL);
        }

        webDriver.get(URL);
        Log.info("Open site: '" + URL + "'");
    }

    /**
     * get properties for webDriver initialization from 'Run.config.properties' file in resources
     * @return Map<String, String>
     * */
    private Map<String, String> getWebDriverProperties() {
        Map<String, String> webDriverProperties = new HashMap<>();
        properties = getProperties("Run.config.properties");
        runType = properties.getProperty("run.type");
        webDriverProperties.put("run.type", runType);
        Log.info("run type = " + runType);
        if (runType.equals("SELENIUM.GRID")) {
            seleniumGridServer = properties.getProperty("selenium.grid");
            webDriverProperties.put("selenium.grid", seleniumGridServer);
            Log.info("selenium.grid server = " + seleniumGridServer);
        }
        browser = properties.getProperty("browser");
        webDriverProperties.put("browser", browser);
        Log.info("browser = " + browser);
        return webDriverProperties;
    }

    @AfterMethod
    public void tearDown() {
        if (webDriver != null) {
            webDriver.quit();
            webDriver = null;
            Log.info("WebDriver quit");
        }
    }
}
