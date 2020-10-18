package com.example.httpsspringboot;

import android.content.Context;
import android.util.Log;

import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class SslUtils {

    public static final String TAG = "----HttpsUtils";

    public static class TrustAllCerts implements X509TrustManager {
        //没用到
        @Override
        public void checkClientTrusted(X509Certificate[] x509Certificates, String s) {
            //do nothing，接受任意客户端证书
            Log.e(TAG, "checkClientTrusted :" + s.toString());
        }

        @Override
        public void checkServerTrusted(X509Certificate[] x509Certificates, String s) {
            //do nothing，接受任意服务端证书
            Log.e(TAG, "checkServerTrusted :" + s.toString());
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            Log.e(TAG, "getAcceptedIssuers :");
            return new X509Certificate[0];
        }
    }

    /**
     * 信任 所有服务器
     *  存在安全问题不能使用，带改进
     * @return
     */
    public static SSLSocketFactory createSSLSocketFactory() {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new TrustAllCerts()}, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
            Log.e(TAG, "TrustAllCerts e :" + e.toString());
        }
        return ssfFactory;
    }


    /**
     * 信任 指定服务器
     *  代码还有问题，bug需要修复， 不能使用
     * @return
     */
    public static SSLSocketFactory createSSLSocketFactory2() {
        SSLSocketFactory ssfFactory = null;
        try {
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, new TrustManager[]{new MyX509TrustManager()}, new SecureRandom());
            ssfFactory = sc.getSocketFactory();
        } catch (Exception e) {
            Log.e(TAG, "TrustAllCerts e :" + e.toString());
        }
        return ssfFactory;
    }

    /**
     * 返回true 信任服务器
     */
    public static class TrustAllHostnameVerifier implements HostnameVerifier {
        @Override
        public boolean verify(String hostname, SSLSession session) {
            // Always return true，接受任意域名服务器
            return true;
        }
    }


    /**
     * 配置客户端证书
     * 和信任的服务器列表, 信任列表中装的就是服务器公钥
     * 双向认证
     *
     * @param context
     */
    public static SSLSocketFactory getSSLSocketFactory(Context context) {
        try {
            // 服务器端需要验证的客户端证书，其实就是客户端的keystore
            // 客户端证书类型
            KeyStore keyStore = KeyStore.getInstance("PKCS12");

            //读取证书
            InputStream ksIn = context.getResources().getAssets().open("client.key.p12");
            //加载证书
            keyStore.load(ksIn, "123456".toCharArray());
            ksIn.close();
            KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance("X509");
            keyManagerFactory.init(keyStore, "123456".toCharArray());


            // 客户端信任的服务器端证书
            KeyStore trustStore = KeyStore.getInstance("PKCS12");
            InputStream tsIn = context.getResources().getAssets().open("client_trust.key.p12");
            trustStore.load(tsIn, "123456".toCharArray());
            tsIn.close();
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);

            //初始化SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(keyManagerFactory.getKeyManagers(), trustManagerFactory.getTrustManagers(), null);

            return sslContext.getSocketFactory();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getSSLSocketFactory: " + e);
        }

        return null;

    }


    /**
     * 单向认证，客户端信任服务器
     *
     * @param context
     * @return
     */
    public static SSLSocketFactory getSSLSocketFactorySingle(Context context) {
        try {

            // 客户端信任的服务器端证书
            KeyStore trustStore = KeyStore.getInstance("PKCS12");
            InputStream tsIn = context.getResources().getAssets().open("client_trust.key.p12");
            trustStore.load(tsIn, "123456".toCharArray());
            tsIn.close();
            TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(trustStore);

            //初始化SSLContext
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

            return sslContext.getSocketFactory();

        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "getSSLSocketFactorySingle: " + e);
        }

        return null;

    }


}