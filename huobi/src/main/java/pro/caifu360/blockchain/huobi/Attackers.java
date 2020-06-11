package pro.caifu360.blockchain.huobi;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author yu.miao
 */
public class Attackers {

    private static final String rootPath = "D:/video/4/";

    private final static Logger logger = LoggerFactory.getLogger(Attackers.class);

    public List<Work> getWorkList(String rootUrl) {
        var options = new ChromeOptions();
        options.addArguments("headless");

        System.setProperty("webdriver.chrome.driver", "C:/dev/8.3/chromedriver.exe");
        var browser = new ChromeDriver();
        // var browser = new ChromeDriver(options);
        browser.get(rootUrl);

        List<Work> workList = new ArrayList<>();

        var workItemList = browser.findElementsByCssSelector(".works-list-item-info");
        for (var workItem : workItemList) {
            var work = new Work();
            var url = workItem.getAttribute("href");
            var id = url.replace("https://www.attackers.net/works/detail/", "");
            id = id.replace("/", "");
            var image = workItem.findElement(By.tagName("img")).getAttribute("src");

            var tag = id.replaceAll("\\d+", "");
            var title = workItem.findElement(By.tagName("p")).getText();

            work.setUrl(url);
            work.setId(id);
            work.setTitle(title);
            work.setTag(tag);
            work.setImage(image);
            workList.add(work);
        }

        for (var work : workList) {
            browser.get(work.getUrl());
            var sampleImageList = browser.findElementsByCssSelector("figure");
            for (var sampleImage : sampleImageList) {
                var sampleImageUrl = sampleImage.findElement(By.tagName("a")).getAttribute("href");
                work.getSampleImageList().add(sampleImageUrl);
            }
        }
        return workList;
    }


    public String mkWorkDir(Work work) {
        var tagPath = FilenameUtils.concat(rootPath, work.getTag());
        var tagDir = new File(tagPath);

        if (tagDir.isDirectory() == false) {
            tagDir.mkdir();
        }

        var workPath = FilenameUtils.concat(tagPath, work.getId());
        workPath = String.format("%s, [%s]", workPath, work.getTitle());
        var workDir = new File(workPath);
        if (workDir.isDirectory() == false) {
            workDir.mkdir();
        }
        return workPath;
    }

    public void download(String url, String path) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(url)
                .get()
                .addHeader("Cache-Control", "no-cache")
                .addHeader("Cookie:", "no-cache")
                .addHeader("User-Agent:", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/83.0.4103.97 Safari/537.36")
                .build();


        Response response = null;
        try {
            response = client.newCall(request).execute();

            var fileName = FilenameUtils.getName(url);
            var filePath = FilenameUtils.concat(path, fileName);
            var file = new File(filePath);


            var bytes = response.body().bytes();
            FileUtils.writeByteArrayToFile(file, bytes);

            logger.info(String.format("Download image [%s] ...", url));
        } catch (IOException e) {
            logger.error("Save file failure: ", e);
        }
    }

    public static void main(String[] args) throws IOException {
        logger.info("Start ...");

        Attackers attackers = new Attackers();
        var list = attackers.getWorkList("https://www.attackers.net/works/list/date/20200507/");
        logger.info("Get work list complete ...");

        for (var work : list) {
            String workDir = attackers.mkWorkDir(work);

            // 下载work 图片
            var url = work.getImage();
            attackers.download(url, workDir);

            for (var imageUrl : work.getSampleImageList()) {
                attackers.download(imageUrl, workDir);
            }
        }

    }

    public class Work {
        private String id;
        private String title;
        private String url;
        private String tag;
        private String image;
        private List<String> sampleImageList = new ArrayList<>();

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public List<String> getSampleImageList() {
            return sampleImageList;
        }

        public void setSampleImageList(List<String> sampleImageList) {
            this.sampleImageList = sampleImageList;
        }
    }
}
