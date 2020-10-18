package com.example.httpsspringboot.okhttp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.httpsspringboot.R;
import com.example.httpsspringboot.Student;
import com.example.httpsspringboot.Urls;
import com.google.gson.Gson;

import java.io.IOException;

public class OkhttpActivity extends AppCompatActivity {


    private static final int MSG_WHAT = 100;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_WHAT:
                    if (msg.obj == null) {
                        tv.setText("请求异常");
                    } else {
                        String json = (String) msg.obj;
                        tv.setText(json);
                    }

            }
        }
    };
    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.data);
        setTitle("OkHttp框架https双向认证");
    }

    private String getUrl = Urls.baseUrl + "get?name=kavin";
    private String postUrl = Urls.baseUrl + "post";


    public void doGet(View view) {

        try {
            GetHttpRequest getHttpRequest = new GetHttpRequest(this, handler);
            getHttpRequest.doGet(getUrl, MSG_WHAT);
        } catch (IOException e) {
            Log.e("----TAG", "doGet--IOException: " + e);
        }
    }

    public void doPost(View view) {

        try {
            Student student = new Student("小明", 18);
            String json = new Gson().toJson(student);

            GetHttpRequest getHttpRequest = new GetHttpRequest(this, handler);
            getHttpRequest.doPost(postUrl, json, MSG_WHAT);
        } catch (IOException e) {
            Log.e("----TAG", "doPost--IOException: " + e);
        }
    }

    public void doPostJson(View view) {

    }
}
