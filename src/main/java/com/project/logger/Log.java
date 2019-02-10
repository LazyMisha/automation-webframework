package com.project.logger;

import com.project.reporter.TestListener;
import io.qameta.allure.Step;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Log extends Level {

    private static Logger Log = Logger.getLogger(Log.class.getName());
    private static final Level STEP = new Log(30000, "STEP", 5);
    private static final Level PASSED = new Log(30000, "PASSED", 5);
    private static final Level START_TEST = new Log(30000, "START_TEST", 5);
    private static final Level FINISH_TEST = new Log(30000, "FINISH_TEST", 5);
    private static final Level START_SUITE = new Log(30000, "START_SUITE", 5);
    private static final Level FINISH_SUITE = new Log(30000, "FINISH_SUITE", 5);

    private Log(int level, String levelStr, int syslogEquivalent) {
        super(level, levelStr, syslogEquivalent);
    }

    @Step("STEP - {step}")
    public static void step(String step) {
        Log.log(STEP, step);
    }

    public static void info(String message) {
        Log.info(message);
    }

    @Step("WARNING - {message}")
    public static void warn(String message) {
        TestListener.isSuccess = false;
        saveScreenShot();
        Log.warn(message);
    }

    @Step("PASSED - {message}")
    public static void passed(String message) {
        saveScreenShot();
        Log.log(PASSED, message);
    }

    @Step("ERROR - {message}")
    public static void error(String message) {
        Log.error(message);
        throw new AssertionError(message);
    }

    @Step("ERROR - {message}")
    public static void error(String message, Throwable throwable) {
        Log.error(message, throwable);
        throw new AssertionError(message, throwable);
    }

    @Step("FATAL - {message}")
    public static void fatal(String message) {
        Log.fatal(message);
    }

    public static void startTest(String testName) {
        Log.log(START_TEST, testName);
    }

    public static void finishTest(String testName) {
        Log.log(FINISH_TEST, testName);
    }

    public static void startSuite(String suiteName) {
        Log.log(START_SUITE, suiteName);
    }

    public static void finishSuite(String suiteName) {
        Log.log(FINISH_SUITE, suiteName);
    }

    private static void saveScreenShot() {
        TestListener.saveScreenshotPNG();
    }
}
