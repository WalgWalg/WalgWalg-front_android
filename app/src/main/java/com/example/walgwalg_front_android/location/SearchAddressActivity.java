package com.example.walgwalg_front_android.location;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.walgwalg_front_android.R;

public class SearchAddressActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        WebView webView=findViewById(R.id.webView);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new BridgeInterface(), "Android");
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                //안드로이드->자바스크립트함수호출
                webView.loadUrl("javascript:sample2_execDaumPostcode();");
            }
        });
//최초웹뷰로드
        webView.loadUrl("https://bica-68326.web.app");
    }

    private class BridgeInterface{
        @JavascriptInterface
        public void processDATA(String data){
            //카카오 주소 검색 API결과 간의 브릿지 통로를 통해 전달(from javascript)
            Intent intent=new Intent();
            intent.putExtra("data",data);
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
