package com.example.httpsspringboot.okhttp;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.httpsspringboot.SslUtils;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GetHttpRequest {

    private OkHttpClient okHttpClient;
    private Request.Builder builder;
    private Handler handler;
    private Context context;


    public GetHttpRequest(Context context, Handler handler) {
        this.handler = handler;
        this.context = context;
        this.okHttpClient = new OkHttpClient.Builder()
                .sslSocketFactory(SslUtils.getSSLSocketFactory(context))
                .hostnameVerifier(new SslUtils.TrustAllHostnameVerifier())
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .build();
        builder = new Request.Builder();

    }

    public void doGet(String url, int msg_what) throws IOException {
        Request request = builder.get().url(url).build();
        executeResponse(request, msg_what);
    }

    public void doPost(String url, String json, int msg_what) throws IOException {
        final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        RequestBody body = RequestBody.create(JSON, json);
        Request request = builder.post(body).url(url).build();
        executeResponse(request, msg_what);
    }

    public void executeResponse(final Request request, final int msg_what) {
        Call call = okHttpClient.newCall(request);

        //SocketTimeoutException连接超时
        call.enqueue(new Callback() {
            @TargetApi(Build.VERSION_CODES.KITKAT)
            @Override
            public void onFailure(Call call, IOException e) {
                Message message = new Message();
                message.what = msg_what;
                message.obj = null;
                handler.sendMessage(message);
                Log.e("-----", "onFailure :" + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                Message message = new Message();
                message.what = msg_what;
                String json = response.body().string();
                message.obj = json;
                handler.sendMessage(message);
                Log.e("-----", "onResponse :" + json);
            }
        });
    }
}