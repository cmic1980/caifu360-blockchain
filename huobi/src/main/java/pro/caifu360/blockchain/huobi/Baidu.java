package pro.caifu360.blockchain.huobi;

import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

/**
 * @author yu.miao
 */
public class Baidu {
    private String driver;

    public Baidu(String driver) {
        this.driver = driver;
    }

    public void go() {
        ChromeOptions options = new ChromeOptions();
        // options.addArguments("-headless");
        System.setProperty("webdriver.chrome.driver", this.driver);
        var browser = new ChromeDriver(options);
        browser.get("https://www.baidu.com");
    }
}
