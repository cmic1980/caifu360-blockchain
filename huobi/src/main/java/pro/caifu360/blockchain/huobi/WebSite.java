package pro.caifu360.blockchain.huobi;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * @author yu.miao
 */
public class WebSite {

    public void go(){
        ChromeOptions options = new ChromeOptions();
        // options.addArguments("-headless");
        System.setProperty("webdriver.chrome.driver", "D:/dev/drivers/chromedriver.exe");
        var browser = new ChromeDriver(options);
        browser.get("https://www.baidu.com");
    }
}
