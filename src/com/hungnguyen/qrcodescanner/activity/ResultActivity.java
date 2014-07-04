package com.hungnguyen.qrcodescanner.activity;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.qrcodescanner.R;

public class ResultActivity extends Activity{
	WebView mWebView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_result);
		mWebView = (WebView)findViewById(R.id.result_webview);
		mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}
			
		});
		Bundle extras = getIntent().getExtras();
		String url = extras.getString("url");
		mWebView.loadUrl(url);
	}
	

}
