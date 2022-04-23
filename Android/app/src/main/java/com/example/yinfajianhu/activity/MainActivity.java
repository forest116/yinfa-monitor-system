package com.example.yinfajianhu.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import com.example.yinfajianhu.R;
import com.example.yinfajianhu.constants.*;
import com.example.yinfajianhu.runnable.InfoRunnable;


public class MainActivity extends AppCompatActivity {

    TextView mInfo, mTime, mSmog, mInvade, mFall,mDementia;
    Button mUnusaul;
    WebView mWebView;

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUi();

        //开启获取环境数据线程
        InfoRunnable infoRunnable = new InfoRunnable(mInfo, mTime, mSmog, mInvade, mFall,mDementia);
        Thread infoThread = new Thread(infoRunnable);
        infoThread.start();

        //开启获取实时图片线程
        //ImageRunable imageRunable = new ImageRunable(mImage);
//        ImageRunable imageRunable = new ImageRunable(mImage,mWebView);
//        Thread imageThread = new Thread(imageRunable);
//        imageThread.start();

        getVedio(NetProperty.inVedioUrl);
        //getVedio(NetProperty.exVedioUrl);

        mUnusaul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,UnusualDataActivity.class);
                startActivity(intent);
            }
        });

    }

    private void initUi() {
        mInfo = findViewById(R.id.info);
        mTime = findViewById(R.id.timenow);
        mSmog = findViewById(R.id.smog);
        mInvade = findViewById(R.id.invade);
        mFall = findViewById(R.id.fall);
        mDementia = findViewById(R.id.dementia);
        mWebView = findViewById(R.id.webview);
        mUnusaul = findViewById(R.id.to_unusaul);
    }

    private void getVedio(String url){
        WebSettings webSettings = mWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);

        mWebView.loadUrl(url);
       // mWebView.loadUrl("http://5107w24g11.zicp.vip/?action=stream");

        mWebView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }





}
