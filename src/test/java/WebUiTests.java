import com.project.Config;
import com.project.ddt.DataProviderManager;
import com.project.logger.Log;
import com.project.reporter.TestListener;
import com.project.utils.TestData;
import io.qameta.allure.*;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Listeners({TestListener.class})
@Epic("Epic: WEB")
@Feature("Feature: Yahoo searching")
public class WebUiTests extends Config {

    @Severity(SeverityLevel.NORMAL)
    @Story("Story: WEB test")
    @Test(description="Description: Test_Yahoo_check_searching_string PASSED"
            , dataProviderClass = DataProviderManager.class, dataProvider = "testData")
    public void Test_Yahoo_check_searching_string(String data) {
        Log.step("Precondition");
        open("Yahoo_start_page");

        Log.step("1. Type info");
        web.yahooSearchPage.typeToSearchField(data);

        Log.step("2. Submit");
        web.yahooSearchPage.submit();

        Log.step("3. Check info");
        web.yahooSearchPage.checkInputted(data.replaceAll(" -.*", ""));
    }

    @Severity(SeverityLevel.NORMAL)
    @Story("Story: WEB test")
    @Test(description="Description: Test_Yahoo_check_searching_string WARNING")
    public void Test_Yahoo_check_searching_string_Warning() {
        Log.step("Precondition");
        open("Yahoo_start_page");
        Log.warn("First warning");

        Log.step("1. Type info");
        web.yahooSearchPage.typeToSearchField(TestData.getFakeCity());

        Log.step("2. Submit");
        web.yahooSearchPage.submit();

        Log.step("3. Check info");
        web.yahooSearchPage.checkInputtedWarning(TestData.getFakeCountry());
    }

    @Severity(SeverityLevel.NORMAL)
    @Story("Story: WEB test")
    @Test(description="Description: Test_Yahoo_check_searching_string FAIL")
    public void Test_Yahoo_check_searching_string_Fail() {
        Log.step("Precondition");
        open("Yahoo_start_page");

        Log.step("1. Type info");
        web.yahooSearchPage.typeToSearchField(TestData.getFakeEmail());

        Log.step("2. Submit");
        web.yahooSearchPage.submit();

        Log.step("3. Check info");
        web.yahooSearchPage.checkInputtedFail(TestData.getFakeFirstName());
    }

    @Severity(SeverityLevel.NORMAL)
    @Story("Story: WEB test")
    @Test(description="Description: Test_Yahoo_check_searching_string WARNING")
    public void Test_Yahoo_check_searching_string_not_data_provider_Warning() {
        Log.step("Precondition");
        open("Yahoo_start_page");
        Log.warn("First warning");

        Log.step("1. Type info");
        web.yahooSearchPage.typeToSearchField(TestData.getPassword());

        Log.step("2. Submit");
        web.yahooSearchPage.submit();

        Log.step("3. Check info");
        web.yahooSearchPage.checkInputtedWarning(String.valueOf(TestData.getRandomDigit(0, 100)));
    }
}
