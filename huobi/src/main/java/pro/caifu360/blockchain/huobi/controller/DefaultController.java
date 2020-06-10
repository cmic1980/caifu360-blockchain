package pro.caifu360.blockchain.huobi.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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

    @PostMapping("/download/article")
    @ResponseBody
    public ResponseEntity<?> download(@RequestBody ArticleDownloadRequest request) throws IOException {
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

}
