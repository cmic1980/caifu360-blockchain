package pro.caifu360.blockchain.huobi.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pro.caifu360.blockchain.huobi.Attackers;
import pro.caifu360.blockchain.huobi.Baidu;
import pro.caifu360.blockchain.huobi.Baidu;
import pro.caifu360.blockchain.huobi.ChineseZj;
import pro.caifu360.blockchain.huobi.model.request.ArticleDownloadRequest;

import java.io.IOException;

/**
 * @author yumiao
 */
@RestController
public class DefaultController {

    @Value("${chrome.driver}")
    private String driver;

    @Value("${server.application.name}")
    private String name;

    @GetMapping("/index")
    public String getList() {
        return this.name;
    }

    @PostMapping("/article/download")
    @ResponseBody
    public ResponseEntity<?> downloadArticle(@RequestBody ArticleDownloadRequest request) throws IOException {
        var self = this;
        var task = new Runnable() {
            @Override
            public void run() {
                ChineseZj chineseZj = new ChineseZj();
                try {
                    chineseZj.download(request.getRootDir(), request.getRootUrl(),request.getBookUrl(), true);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        var thread = new Thread(task);
        thread.start();


        JSONObject jsonObject = new JSONObject();
        return ResponseEntity.ok().body(jsonObject);
    }


    @PostMapping("/attackers/download")
    @ResponseBody
    public ResponseEntity<?> downloadAttackers(@RequestBody ArticleDownloadRequest request) throws IOException {
        var self = this;
        var task = new Runnable() {
            @Override
            public void run() {
                Attackers attackers = new Attackers();
                var list = attackers.getWorkList("https://www.attackers.net/works/list/date/20200507/");
                System.out.println("Get work list complete ...");


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
        };

        var thread = new Thread(task);
        thread.start();


        JSONObject jsonObject = new JSONObject();
        return ResponseEntity.ok().body(jsonObject);
    }
}
