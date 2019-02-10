#!/usr/bin/env bash
#java -jar -Dwebdriver.chrome.driver="chromedriver" selenium-server-standalone-3.14.0.jar -role node -hub http://192.168.1.14:4444/grid/register -browser "browserName=chrome"
java -jar -Dwebdriver.chrome.driver="chromedriver" selenium-server-standalone-3.141.5.jar -role node -hub http://localhost:4444/grid/register -browser "browserName=chrome"