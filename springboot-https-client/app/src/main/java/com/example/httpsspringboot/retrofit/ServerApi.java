package com.example.httpsspringboot.retrofit;

import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface ServerApi {

    @GET("get")//指定该方法要请求的url，注意方法的路径不要以/开头，如/test，是错误的
    Call<ResponseInfo> get();

    @FormUrlEncoded//配置对请求体参数进行url编码
    @POST("post")
    Call<ResponseInfo> post(@Field("username")String username, @Field("password")String password);

    @POST("postJson")
    Call<JsonObject> postJson(@Body JsonObject jsonObject);

}