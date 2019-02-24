package com.project.page.yahooSearchePage;

import com.project.common.CommonPage;
import com.project.logger.Log;
import com.project.utils.Check;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.is;

public class YahooSearchPage extends CommonPage {

    public void typeToSearchField(String string) {
        element("Searching_field").set(string);
        Check.that("Check inputted", string, is(element("Searching_field").getAttribute("value")));
    }

    public void submit() {
        element("Searching_button").click();
    }

    public void checkInputted(String expected) {
        List<WebElement> foundList = element("FoundInfoList").getWebElements();
        boolean isContains = false;

        for (int i = 0; i < foundList.size(); i++) {
            String actual = element("FoundInfoList_" + i, foundList.get(i)).getText();
            isContains = actual.contains(expected);
            if (isContains) {
                Check.that("Found info: '" + expected + "'", actual, containsString(expected));
                break;
            }
        }

        if (!isContains) {
            Log.warn("Lines are not contains: '" + expected + "'");
        }
    }

    public void checkInputtedWarning(String expected) {
        List<WebElement> foundList = element("FoundInfoList").getWebElements();
        Log.warn("Lines are not contains: '" + expected + "'");
    }

    public void checkInputtedFail(String expected) {
        throw new AssertionError(expected);
    }
}
