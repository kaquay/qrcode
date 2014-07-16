package com.hungnguyen.qrcodescanner.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.qrcodescanner.R;

public class ResultActivity extends Activity implements OnClickListener {
	WebView mWebView;
	ImageButton mIbShare;
	Button mBtBack;
	TextView mTvTitle;
	String url;
	RelativeLayout mLayoutActionBar;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getActionBar().hide();
		setContentView(R.layout.activity_result);
		mWebView = (WebView) findViewById(R.id.result_webview);
		mBtBack = (Button) findViewById(R.id.result_bt_back);
		mIbShare = (ImageButton) findViewById(R.id.result_ib_share);
		mTvTitle = (TextView) findViewById(R.id.result_tv_title);
		mLayoutActionBar =(RelativeLayout)findViewById(R.id.result_rl_actionbar);
		mBtBack.setOnClickListener(this);
		mIbShare.setOnClickListener(this);

		mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				mTvTitle.setText("" + view.getTitle());
			}

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

		});
		
		int screenHeight = getResources().getDisplayMetrics().heightPixels;
		int heightBar = screenHeight * 10 / 100; // 10% of creenHeight
		LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mLayoutActionBar.getLayoutParams();
		params.height = heightBar;
		mLayoutActionBar.setLayoutParams(params);
		
		Bundle extras = getIntent().getExtras();
		url = extras.getString("url");
		mWebView.loadUrl(url);
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.result_bt_back:
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
