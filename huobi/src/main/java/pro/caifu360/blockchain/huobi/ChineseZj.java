package pro.caifu360.blockchain.huobi;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class ChineseZj {
    public void download(String rootDir, String rootUrl, String bookUrl) throws IOException {
        this.download(rootDir, rootUrl, bookUrl, true);
    }

    public void download(String rootDir, String rootUrl, String bookUrl, boolean addTitle) throws IOException {
        var options = new ChromeOptions();
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");

        System.setProperty("webdriver.chrome.driver", "C:/dev/8.3/chromedriver.exe");
        var browser = new ChromeDriver(options);
        // var browser = new ChromeDriver(options);

        var url = rootUrl + bookUrl;
        browser.get(url);

        // ???? title, ?title ?????
        var h1 = browser.findElementByCssSelector(".info2 h1");
        var bookDir = h1.getText();
        bookDir = FilenameUtils.concat(rootDir, bookDir);

        // File file = new File(bookDir.substring(0,bookDir.length()-1) + ".txt");
        File file = new File(bookDir + ".txt");
        if (file.exists()) {
            file.delete();
        }
        file.createNewFile();

        // ?? ?? ??
        // ??????
        List<String> detailPageUrlList = new ArrayList<>();
        var list = browser.findElementsByCssSelector(".list-group li");
        for (var li : list) {
            var a = li.findElement(By.tagName("a"));
            var detailPageUrl = a.getAttribute("href");
            detailPageUrlList.add(detailPageUrl);
        }

        // ????????
        var charset = Charset.defaultCharset();
        var i = 0;
        for (var detailPageUrl : detailPageUrlList) {
            i = i + 1;
            browser.get(detailPageUrl);

            var panelHeading = browser.findElementByCssSelector(".panel-heading");
            var title = panelHeading.getText();
            if (addTitle) {
                title = "第 " + i + " 章  " + title;
            }

            FileUtils.write(file, title, charset, true);

            FileUtils.write(file, "\r\n", charset, true);
            FileUtils.write(file, "\r\n", charset, true);

            var panelBody = browser.findElementByCssSelector(".panel-body");
            var body = panelBody.getText();

            /*
            var fileName = FilenameUtils.concat(bookDir, title + ".txt");
            file = new File(fileName);
             */
            FileUtils.write(file, body, charset, true);
            FileUtils.write(file, "\r\n", charset, true);
            FileUtils.write(file, "------------", charset, true);

            FileUtils.write(file, "\r\n", charset, true);
            FileUtils.write(file, "\r\n", charset, true);
            FileUtils.write(file, "\r\n", charset, true);
            FileUtils.write(file, "\r\n", charset, true);
            FileUtils.write(file, "\r\n", charset, true);

        }
    }

    public static void main(String[] args) throws IOException {
        var rootDir = args[0];
        var rootUrl = args[1];
        var bookUrl = args[2];

        ChineseZj chineseZj = new ChineseZj();
        chineseZj.download(rootDir,rootUrl,bookUrl, true );
    }
}
