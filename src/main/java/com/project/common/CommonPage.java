package com.project.common;

import com.project.Config;
import org.openqa.selenium.WebElement;

public class CommonPage extends Config {

    protected Element element(String field) {
        return new Element(field, getWebDriver());
    }

    protected Element element(String webElementName, WebElement webElement) {
        return new Element(webElementName, webElement, getWebDriver());
    }
}
