package com.project.utils;

import com.project.logger.Log;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;

public class Check {

    public static <T> void that(T actual, Matcher<? super T> matcher) {
        that("Check", actual, matcher);
    }

    public static <T> void that(String reason, T actual, Matcher<? super T> matcher) {
        Description description = new StringDescription();
        description.appendText(reason).appendText("\nExpected: ").appendDescriptionOf(matcher).appendText("\nActual: ");
        matcher.describeMismatch(actual, description);
        if (!matcher.matches(actual)) {
            Log.warn(description.toString());
        } else {
            Log.passed(reason + "\n" + "Actual: '" + actual + "' \n" + "Expected '" + matcher + "'");
        }
    }

    public static void that(String reason, boolean assertion) {
        Description description = new StringDescription();
        description.appendText(reason).appendText("\nExpected: 'true'").appendText("\nbut: ").appendText(String.valueOf(assertion));
        if (assertion) {
            Log.passed(reason + "\n" + "assertion: '" + true + "'");
        } else {
            Log.error(reason + "\n" + "assertion: '" + false + "'", new AssertionError(description.toString()));
        }
    }

    public static void fail(String message) {
        Log.fatal(message);
    }
}
