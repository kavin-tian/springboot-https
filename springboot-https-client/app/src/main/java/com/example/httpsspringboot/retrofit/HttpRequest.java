package com.example.httpsspringboot.retrofit;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.httpsspringboot.MyApp;
import com.example.httpsspringboot.Student;
import com.example.httpsspringboot.TrustAllCerts;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class HttpRequest {

    private final ServerApi serverApi;
    private static HttpRequest mHttpRequest;


    private HttpRequest() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .sslSocketFactory(TrustAllCerts.getSSLSocketFactory(MyApp.appContext))
                .hostnameVerifier(new TrustAllCerts.TrustAllHostnameVerifier())
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();

        //创建Retrofit
        Retrofit retrofit = new Retrofit.Builder()
                //注意，服务器主机应该以/结束，
                .baseUrl(Urls.BASE_URL)//设置服务器主机
                .addConverterFactory(GsonConverterFactory.create())//配置Gson作为json的解析器
                .client(okHttpClient)
                .build();
        serverApi = retrofit.create(ServerApi.class);
    }

    public static synchronized HttpRequest getInstance() {

        if (mHttpRequest == null) {
            mHttpRequest = new HttpRequest();
            return mHttpRequest;
        } else {
            return mHttpRequest;
        }
    }

    public void doGet(final Handler handler, final int msgWhat) throws IOException {


        //调用业务方法，得到要执行的业务请求对象
        Call<ResponseInfo> call = serverApi.get();

        //异步执行业务方法
        call.enqueue(new retrofit2.Callback<ResponseInfo>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseInfo> call, retrofit2.Response<ResponseInfo> response) {
                Message message = new Message();
                message.what = msgWhat;
                ResponseInfo responseInfo = response.body();
                String data = responseInfo.getData();
                Student student = new Gson().fromJson(data, Student.class);
                String json = new Gson().toJson(student);
                message.obj = json;
                handler.sendMessage(message);
                Log.e("-----", "onResponse :" + json);
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseInfo> call, Throwable t) {
                Message message = new Message();
                message.what = msgWhat;
                message.obj = null;
                handler.sendMessage(message);
                Log.e("-----", "onFailure :" + t);
            }
        });


    }

    public void doPost(final Handler handler, final int msgWhat) throws IOException {

        //调用业务方法，得到要执行的业务请求对象
        Call<ResponseInfo> call = serverApi.post("小明", "123456");

        //异步执行业务方法
        call.enqueue(new retrofit2.Callback<ResponseInfo>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseInfo> call, retrofit2.Response<ResponseInfo> response) {
                Message message = new Message();
                message.what = msgWhat;
                ResponseInfo responseInfo = response.body();
                String json = new Gson().toJson(responseInfo);
                message.obj = json;
                handler.sendMessage(message);
                Log.e("-----", "onResponse :" + json);
            }

            @Override
            public void onFailure(retrofit2.Call<ResponseInfo> call, Throwable t) {
                Message message = new Message();
                message.what = msgWhat;
                message.obj = null;
                handler.sendMessage(message);
                Log.e("-----", "onFailure :" + t);
            }
        });

    }

    public void doPostJson(final Handler handler, final int msgWhat) throws IOException {
        ResponseInfo postJson = new ResponseInfo("110", "postJson");
        String json = new Gson().toJson(postJson);

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("code", "110");
        jsonObject.addProperty("data", "postJson");

        //调用业务方法，得到要执行的业务请求对象
        Call<JsonObject> call = serverApi.postJson(jsonObject);
        //异步执行业务方法
        call.enqueue(new retrofit2.Callback<JsonObject>() {
            @Override
            public void onResponse(retrofit2.Call<JsonObject> call, retrofit2.Response<JsonObject> response) {
                Message message = new Message();
                message.what = msgWhat;
                JsonObject responseInfo = response.body();
                String json = new Gson().toJson(responseInfo);
                message.obj = json;
                handler.sendMessage(message);
                Log.e("-----", "onResponse :" + json);
            }

            @Override
            public void onFailure(retrofit2.Call<JsonObject> call, Throwable t) {
                Message message = new Message();
                message.what = msgWhat;
                message.obj = null;
                handler.sendMessage(message);
                Log.e("-----", "onFailure :" + t);
            }
        });
    }
}