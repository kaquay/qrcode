package com.hungnguyen.qrcodescanner.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.qrcodescanner.R;

public class ResultActivity extends Activity implements OnClickListener {
	WebView mWebView;
	ImageButton mIbBack, mIbShare;
	TextView mTvTitle;
	String url;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getActionBar().hide();
		setContentView(R.layout.activity_result);
		mWebView = (WebView) findViewById(R.id.result_webview);
		mIbBack = (ImageButton) findViewById(R.id.result_ib_back);
		mIbShare = (ImageButton) findViewById(R.id.result_ib_share);
		mTvTitle = (TextView) findViewById(R.id.result_tv_title);
		mIbBack.setOnClickListener(this);
		mIbShare.setOnClickListener(this);

		mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

		});
		Bundle extras = getIntent().getExtras();
		url = extras.getString("url");
		mWebView.loadUrl(url);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.result_ib_back:
			finish();
			break;
		case R.id.result_ib_share:
			Intent sendIntent = new Intent();
			sendIntent.setAction(Intent.ACTION_SEND);
			sendIntent.putExtra(Intent.EXTRA_TEXT, url);
			sendIntent.setType("text/plain");
			startActivity(Intent.createChooser(sendIntent, ""));
			break;
		}
	}

	@Override
	public void onBackPressed() {
		finish();
	}

}
