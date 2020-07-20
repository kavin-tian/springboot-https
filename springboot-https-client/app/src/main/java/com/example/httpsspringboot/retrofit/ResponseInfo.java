package com.example.httpsspringboot.retrofit;

/**
 * 封装网络请求结果
 * 服务器端返回json
 *  {
 *      "code":"0",
 *      "data":"具体文本内容"
 *   }
 */
public class ResponseInfo {
    private String code;
    private String data;

    public ResponseInfo(String code, String data) {
        this.code = code;
        this.data = data;
    }

    public String getCode() {
        return code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    public String getData() {
        return data;
    }
    public void setData(String data) {
        this.data = data;
    }
}