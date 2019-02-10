package com.project.common;

import com.project.logger.Log;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

import static com.project.utils.FileSystem.getPathLocators;

public class Element {

    private WebDriver webDriver;
    private String field;
    private String xpath;
    private Actions actions;
    private WebElement element;

    /**
     * @param webDriver - initialize WebDriver webDriver
     * @param field - initialize String field
     * - initialize Actions actions
     * - get xpath by field name
     * - initialize WebElement element
     * - move to element on the page using
     * */
    Element(String field, WebDriver webDriver) {
        this.webDriver = webDriver;
        this.field = field;
        actions = new Actions(webDriver);
        xpath = getLocator();
        try {
            Log.info("Wait for '" + field + "' .. 60 seconds");
            element = (new WebDriverWait(webDriver, 60)).until(ExpectedConditions.elementToBeClickable(By.xpath(xpath)));
        } catch (Exception e) {
            Log.error("Can not get element '" + field + "', xpath = " + xpath, e);
        }
        actions.moveToElement(element);
    }

    /**
     * @param webElementName - initialize String field
     * @param webDriver - initialize WebDriver webDriver
     * @param webElement - initialize WebElement element
     * - initialize Actions actions
     * - move to element on the page using
     * */
    Element(String webElementName, WebElement webElement, WebDriver webDriver) {
        this.webDriver = webDriver;
        field = webElementName;
        element = webElement;
        xpath = "no xpath";
        actions = new Actions(webDriver);
        actions.moveToElement(element);
    }

    public void set(CharSequence... keys) {
        changeBorderOfElement(true);
        String key = Arrays.toString(keys);
        Log.info("Try to set: '" + key + "' to the: '" + field + "'" + " xpath: " + xpath);
        changeBorderOfElement(false);
        element.sendKeys(keys);
    }

    public void click() {
        changeBorderOfElement(true);
        Log.info("Try to click the: '" + field + "'");
        changeBorderOfElement(false);
        element.click();
    }

    public List<WebElement> getWebElements() {
        Log.info("Try to get all WebElements: '" + field + "' by xpath: " + xpath);
        return webDriver.findElements(By.xpath(xpath));
    }

    public String getAttribute(String attribute) {
        changeBorderOfElement(true);
        Log.info("Try to get value from: '" + attribute + "' attribute. Field: '" + field + "'" + "\n" + "find by xpath: " + xpath);
        String result = element.getAttribute(attribute);
        Log.info("'" + attribute + "' has '" + result + "'");
        changeBorderOfElement(false);
        return result;
    }

    public String getText() {
        changeBorderOfElement(true);
        Log.info("Try to get text from field: '" + field + "'. Find by xpath: " + xpath);
        String result = element.getText();
        Log.info("'" + field + "' has text '" + result + "'");
        changeBorderOfElement(false);
        return result;
    }

    /**
     * this method mark current element (orange color)
     * @param highlight - use 'true' when element initialized and 'false' before some action and after logging
     * */
    private void changeBorderOfElement(boolean highlight) {
        try {
            JavascriptExecutor js = (JavascriptExecutor) webDriver;
            String style = element.getAttribute("style");
            if (!highlight) {
                js.executeScript("arguments[0].setAttribute('style', arguments[1]);",
                        element, style.replace("border: 2px solid orange;", ""));
            } else {
                js.executeScript("arguments[0].setAttribute('style', arguments[1]);",
                        element, style + "border: 2px solid orange;");
            }
        } catch (NoSuchWindowException e) {
            Log.info("Element is not marking as bordered");
        }
    }

    /**
     * this method returns locator by field value
     * */
    private String getLocator() {
        try {
            Properties properties = new Properties();
            properties.load(new FileInputStream(System.getProperty("user.dir") + getPathLocators()));
            return properties.getProperty(field);
        } catch (IOException e) {
            Log.warn("Can not get locator for '" + field + "'");
        }
        return "";
    }
}
