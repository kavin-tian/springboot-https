package com.example.httpsspringboot.retrofit;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.httpsspringboot.R;
import com.example.httpsspringboot.okhttp.OkhttpActivity;

import java.io.IOException;

public class RetrofitActivity extends AppCompatActivity {

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
        setContentView(R.layout.activity_retrofit);
        tv = findViewById(R.id.data);
        setTitle("Retrofit框架https双向论证");

    }

    public void doGet(View view) {

        try {
            HttpRequest httpRequest = HttpRequest.getInstance();
            httpRequest.doGet(handler,MSG_WHAT);
        } catch (IOException e) {
            Log.e("----TAG", "doGet--IOException: " + e);
        }
    }

    public void doPost(View view) {

        try {
            HttpRequest httpRequest = HttpRequest.getInstance();
            httpRequest.doPost(handler,MSG_WHAT);
        } catch (IOException e) {
            Log.e("----TAG", "doGet--IOException: " + e);
        }

    }

    public void doPostJson(View view) {

        try {
            HttpRequest httpRequest = HttpRequest.getInstance();
            httpRequest.doPostJson(handler,MSG_WHAT);
        } catch (Exception e) {
            Log.e("----TAG", "doGet--IOException: " + e);
        }

    }

    public void openOkhttp(View view) {
        Intent intent = new Intent(this, OkhttpActivity.class);
        startActivity(intent);
    }

    public void openSingle(View view) {
        Intent intent = new Intent(this, SingleActivity.class);
        startActivity(intent);
    }
}
