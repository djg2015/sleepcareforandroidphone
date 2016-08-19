package com.ewell.android.sleepcareforphone.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.ewell.android.sleepcareforphone.R;

/**
 * Created by lillix on 7/18/16.
 */
public class ReadProtocolActivity extends Activity {

    private WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_readprotocol);
        OpenProtocol();

    }

    private void  OpenProtocol(){
        webView = (WebView) findViewById(R.id.webView);
        //WebView加载web资源
        webView.loadUrl("http://www.usleepcare.com/app/protocol.aspx");
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
    }

    public void ClickClose(View view){

        this.finish();
    }
}
