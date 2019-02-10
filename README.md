# automation-webframework

- Java 8
- TestNG
- DataProvider
- Log4j report
- Allure report with screenshots
- Send email with results (Allure report) (.zip)
- Local run and Selenium Grid run
- Hamcrest matcher
- JavaFaker for test generate data

- Navigate to the '/resources/Run.config.properties' file
- Install what you need

For local run
 - 
 Run via CMD - 'mvn clean compile test -Dsuite=suiteName'

For selenium grid run
 -
 - Navigate to the '/resources/linux' or '/resources/windows' (depends on system you are using)
 - download download selenium-standalone-server and put to the folder
 - Run 'hub' and next run 'node' commands
 - Run via CMD - 'mvn clean compile test -Dsuite=suiteName'

Get allure report for WINDOWS
 -
 - In order to generate a report, just install Allure command-line interpreter
 - Download the latest version as a zip archive from 'https://bintray.com/qameta/generic/allure2'
 - Unpack the archive to allure-commandline directory
 - Navigate to bin directory and copy path
 - Add allure to system PATH
 - And finally, open a command prompt screen, go to the project directory
 - Run via CMD 'allure serve allure-results'

Get allure report for LINUX
 -
 - Download the latest version as a zip archive from 'https://bintray.com/qameta/generic/allure2'
 - Unpack the archive to allure-commandline directory
 - Copy 'allure-results' folder from the project root after tests running
 - Navigate to allure bin directory
 - Past the folder
 - And finally, open a command prompt screen and cd to allure bin directory
 - Run via CMD - './allure serve allure-results'

Email - Optional
 -
 - Use @gmail.com email address
 - Navigate to '/resources/Run.config.properties' file
 - Set 'send.report=true'
 - Set your 'email' and 'password'
 - Set 'recipients' which will be receive email. Separate by comma (,)
 - You should open access to unsafe applications - 'https://myaccount.google.com/lesssecureapps'
 - Check email after running suite