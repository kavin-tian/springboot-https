package com.example.springboothttps;

import com.alibaba.fastjson.JSONObject;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class Demo {
    private SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
    @GetMapping("/get")
    public String get(HttpServletRequest request) {

        String name = request.getParameter("name");
        System.out.println(name);

        String datetime = mSimpleDateFormat.format(new Date());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("name", "kavin");
        jsonObject.put("age", "18");
        jsonObject.put("datetime", datetime);

        JSONObject result = new JSONObject();
        result.put("code", "0");
        result.put("data", jsonObject.toJSONString());

        System.out.println(result.toJSONString());

        return result.toJSONString();
    }

    @PostMapping("/post")
    public String post(HttpServletRequest request) {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        System.out.println(username + ":" + password);

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username", username);
        jsonObject.put("password", password);

        JSONObject result = new JSONObject();
        result.put("code", "0");
        result.put("data", jsonObject.toJSONString());


        System.out.println(result.toJSONString());

        return result.toJSONString();
    }

    /**
     * 功能描述:通过request的方式来获取到json数据<br/>
     * jsonobject 这个是阿里的 fastjson对象
     */
    @ResponseBody
    @RequestMapping(value = "/postJson", method = RequestMethod.POST, produces = "application/json;charset=UTF-8")
    public String getByJSON(@RequestBody JSONObject jsonParam) {


        // 将获取的json数据封装一层，然后在给返回
        JSONObject result = new JSONObject();
        result.put("method", "json");
        result.put("data", jsonParam);

        // 直接将json信息打印出来
        System.out.println(result.toJSONString());
        return result.toJSONString();
    }

}
