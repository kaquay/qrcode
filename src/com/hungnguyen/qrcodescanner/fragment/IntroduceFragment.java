package com.hungnguyen.qrcodescanner.fragment;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnKeyListener;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.qrcodescanner.R;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class IntroduceFragment extends Fragment {
	WebView mWebView;

	public IntroduceFragment() {
		super();
	}

	// public static IntroduceFragment newInstance(String url) {
	// IntroduceFragment fragment = new IntroduceFragment();
	// Bundle args = new Bundle();
	// args.putString("url", url);
	// fragment.setArguments(args);
	// return fragment;
	// }

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_introduce, container,
				false);
		mWebView = (WebView) view.findViewById(R.id.introduce_webview);
		mWebView.setWebViewClient(new WebViewClient() {

			@Override
			public boolean shouldOverrideUrlLoading(WebView view, String url) {
				view.loadUrl(url);
				return true;
			}

		});
		return view;

	}

	@Override
	public void onStart() {
		super.onStart();
		String html = getActivity().getResources().getString(R.string.about);
		mWebView.loadDataWithBaseURL("", html, "text/html", "utf-8", "");

	}

}
