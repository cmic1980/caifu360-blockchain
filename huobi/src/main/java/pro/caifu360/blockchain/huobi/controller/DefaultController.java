package pro.caifu360.blockchain.huobi.controller;

import com.alibaba.fastjson.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pro.caifu360.blockchain.huobi.WebSite;

/**
 * @author yumiao
 */
@RestController
public class DefaultController {
    @GetMapping("/index")
    @ResponseBody
    public ResponseEntity<?> getList() {

        var task = new Runnable() {
            @Override
            public void run() {
                var webSite = new WebSite();
                webSite.go();
            }
        };

        var thread = new Thread(task);
        thread.start();

        JSONObject jsonObject = new JSONObject();
        return ResponseEntity.ok().body(jsonObject);
    }
}
