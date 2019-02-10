package com.project.reporter;

import com.project.Config;
import com.project.logger.Log;
import com.project.utils.EMail;
import com.project.utils.FileSystem;
import io.qameta.allure.Attachment;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.util.Properties;

public class TestListener extends Config implements ITestListener {

    public static boolean isSuccess = true;

    private static String getTestMethodName(ITestResult iTestResult) {
        return iTestResult.getMethod().getConstructorOrMethod().getName();
    }

    /**
     * Screen attachments for Allure Report
     */
    @Attachment(value = "Page screenshot", type = "image/png")
    public static byte[] saveScreenshotPNG() {
        return ((TakesScreenshot)getWebDriver()).getScreenshotAs(OutputType.BYTES);
    }

    /**
     * Text attachments for Allure Report
     * */
    @Attachment(value = "{message}", type = "text/plain")
    private static String saveTextLog(String message) {
        return message;
    }

    /**
     * HTML attachments for Allure Report
     * */
    @Attachment(value = "{html}", type = "text/html")
    public static String attachHtml(String html) {
        return html;
    }

    /**
     * Start suite
     * */
    @Override
    public void onStart(ITestContext iTestContext) {
        Log.startSuite("name = " + iTestContext.getSuite().getName());
        iTestContext.setAttribute("WebDriver", getWebDriver());
    }

    /**
     * Finish suite
     * */
    @Override
    public void onFinish(ITestContext iTestContext) {
        Log.finishSuite("name = " + iTestContext.getSuite().getName());
        Properties properties = FileSystem.getProperties("Run.config.properties");
        if (Boolean.valueOf(properties.getProperty("send.report"))) {
            EMail.send("Automation run results", "<h1>FINISHED</h1></br>" + iTestContext.getSuite().getName(), true);
        }
    }

    /**
     * Start test
     * */
    @Override
    public void onTestStart(ITestResult iTestResult) {
        Log.startTest("name = " + getTestMethodName(iTestResult));
        Log.info("Description: " + iTestResult.getMethod().getDescription());
        Log.info("Priority: " + iTestResult.getMethod().getPriority());
    }

    /**
     * Finish test
     * ON SUCCESS or WARNING
     * */
    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        String textLog = "name = " +  getTestMethodName(iTestResult) + " is " + (isSuccess ? "SUCCESS" : "WARNING");
        Log.finishTest(textLog);
        if (!isSuccess) {
            iTestResult.setStatus(ITestResult.FAILURE);
            isSuccess = true;
        }
        saveTextLog(textLog);
    }

    /**
     * Finish test
     * ON FAILURE
     * */
    @Override
    public void onTestFailure(ITestResult iTestResult) {
        Log.finishTest("name = " +  getTestMethodName(iTestResult) + " is FAILED");
        Log.info("Screenshot captured for test case:" + getTestMethodName(iTestResult));
        saveScreenshotPNG();
        String textLog = getTestMethodName(iTestResult) + " is FAILED and screenshot taken!";
        saveTextLog(textLog);
    }

    /**
     * Finish test
     * ON SKIP
     * */
    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        Log.finishTest("name = "+  getTestMethodName(iTestResult) + " is skipped");
    }

    /**
     * Finish test
     * */
    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {
        Log.finishTest("name = " + getTestMethodName(iTestResult) + ". Test failed but it is in defined success ratio");
    }

}
