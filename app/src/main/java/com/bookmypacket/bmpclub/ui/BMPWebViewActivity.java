package com.bookmypacket.bmpclub.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.bookmypacket.bmpclub.R;

import static android.view.View.SCROLLBARS_INSIDE_OVERLAY;
import static com.bookmypacket.bmpclub.R.id.wv_container;
import static com.bookmypacket.bmpclub.R.layout.layout_webview;
import static com.bookmypacket.bmpclub.utils.AppConstants.BundleTags.CONTAINER_URL;

/**
 * Created by Manish on 25-11-2015.
 */
public class BMPWebViewActivity extends AppCompatActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(layout_webview);
        WebView     wv       = (WebView) findViewById(wv_container);
        WebSettings settings = wv.getSettings();
        settings.setJavaScriptEnabled(true);
        wv.setScrollBarStyle(SCROLLBARS_INSIDE_OVERLAY);
        String      url      = getIntent().getStringExtra(CONTAINER_URL);
        wv.loadUrl(url);
    }
}
