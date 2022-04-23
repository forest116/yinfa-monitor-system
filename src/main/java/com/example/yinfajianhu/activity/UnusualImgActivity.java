package com.example.yinfajianhu.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.admin.DelegatedAdminReceiver;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.yinfajianhu.R;

import org.json.JSONException;
import org.json.JSONObject;

public class UnusualImgActivity extends AppCompatActivity {

    WebView mUnusaulImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_unsual_img);
        String data = getIntent().getStringExtra("data");
        Log.d("-------",data);

        mUnusaulImg = findViewById(R.id.unusaul_img);

        try {
            JSONObject jsonObject = new JSONObject(data);
            String time = jsonObject.getString("time");
            Log.d("---------",time);
            getVedio("http://ksjddg.cn/hk/unusual-images/"+time+".jpg");

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getVedio(String url){
        WebSettings webSettings = mUnusaulImg.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        webSettings.setLoadWithOverviewMode(true);

        mUnusaulImg.loadUrl(url);
//        mWebView.loadUrl("http://5107w24g11.zicp.vip");

        mUnusaulImg.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

}
