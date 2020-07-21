package com.example.httpsspringboot.retrofit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.httpsspringboot.R;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManagerFactory;

public class SingleActivity extends AppCompatActivity {

    private TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);
        setTitle("httpsURLConnection单向认证");
        tv = findViewById(R.id.data);
    }

    public void doGet(View view) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                conn1();
            }
        }).start();

    }

    private void conn1() {
        try {
            //获取公钥信息
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            String algorithm = TrustManagerFactory.getDefaultAlgorithm();
            TrustManagerFactory tmf = TrustManagerFactory
                    .getInstance(algorithm);
            //删除默认的公钥
            keyStore.load(null);
            String alias = "server";
            //设置公钥的类型
            CertificateFactory factory = CertificateFactory.getInstance("X.509");
            //获取公钥资源
            InputStream tsIn = getResources().getAssets().open("server.cer");
            //获取公钥对象
            Certificate cert = factory.generateCertificate(tsIn);
            //设置自己的公钥
            keyStore.setCertificateEntry(alias, cert);
            //初始化公钥
            tmf.init(keyStore);

            //初始化SSLContext
            SSLContext context = SSLContext.getInstance("TLS");
            context.init(null, tmf.getTrustManagers(), null);
            //通过HttpsURLConnection设置链接
            URL url = new URL("https://192.168.0.104:8443/get");
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) url
                    .openConnection();
            httpsURLConnection.setSSLSocketFactory(context.getSocketFactory());
            /***********************************************************************/
            //设置信任主机ip
            httpsURLConnection.setHostnameVerifier(new HostnameVerifier() {

                @Override
                public boolean verify(String hostname, SSLSession session) {
                    //return true 表示信任主机ip
                    return true;
                }
            });
            /***********************************************************************/
            //获取服务器反馈的数据
            InputStream in = httpsURLConnection.getInputStream();
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            byte[] b = new byte[1024];
            int len = -1;
            while ((len = in.read(b)) != -1) {
                outputStream.write(b, 0, len);
            }
            String content = outputStream.toString();

            Log.e("-----", content);

            updataView(content);

        } catch (Exception e) {
            e.printStackTrace();
            updataView("Exception: "+e.getMessage());
        }
    }

    private void updataView(final String content) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                tv.setText(content);
            }
        });
    }

}
