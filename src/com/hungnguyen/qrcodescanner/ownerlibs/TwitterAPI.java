package com.hungnguyen.qrcodescanner.ownerlibs;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;
import twitter4j.conf.ConfigurationBuilder;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.qrcodescanner.R;

public class TwitterAPI {

	/**
	 * Consumer key
	 * 
	 * @link https://apps.twitter.com/
	 */
	private String CONSUMER_KEY;
	/**
	 * Consumer secret key
	 * 
	 * @link https://apps.twitter.com/
	 */
	private String CONSUMER_SECRET;

	/**
	 * Callback URL
	 */
	private String CALLBACK_URL;

	/**
	 * 
	 */
	private Twitter mTwitter;

	/**
	 * RequestToken
	 * 
	 */
	private RequestToken mRequestToken;
	private static final String URL_TWITTER_AUTH = "auth_url";
	private static final String URL_TWITTER_OAUTH_VERIFIER = "oauth_verifier";
	private static final String URL_TWITTER_OAUTH_TOKEN = "oauth_token";
	private Activity mActivity;
	private TwitterLoginListener mTwitterLoginListener;
	private Dialog mDialog;
	private AccessToken mAccessToken;
	private String mOauthVerifier;
	private TwitterPostStatusListener mPostStatusListener;

	public TwitterAPI(Activity activity) {
		this.mActivity = activity;

	}

	/**
	 * show Dialog to login twitter
	 * 
	 * @throws Exception
	 */
	public void showDialogLogin(TwitterLoginListener listener) {
		this.mTwitterLoginListener = listener;
		try {
			config();
			mDialog = new Dialog(mActivity,
					R.style.CustomDialogThemeNoTitleFullScreen);
			mDialog.setContentView(R.layout.dialoglogin);
			WebView wv = (WebView) mDialog.findViewById(R.id.wvlogin);
			WebSettings webSettings = wv.getSettings();
			webSettings.setJavaScriptEnabled(true);
			wv.setWebViewClient(webviewclient);
			wv.loadUrl(mRequestToken.getAuthorizationURL());
			mDialog.show();
		} catch (Exception e) {
			Log.d("TwitterAPI", "" + e);
		}
	}

	public boolean isAlreadyLogin() {
		boolean result = true;
		if (mTwitter == null)
			result = false;
		if (mAccessToken == null)
			result = false;
		if (mOauthVerifier == null || mOauthVerifier.equals(""))
			result = false;
		return result;
	}

	public void postStatus(String status, TwitterPostStatusListener listener) {
		if (!isAlreadyLogin())
			return;
		this.mPostStatusListener = listener;
		new postAsyntask().execute(status);
	}

	private void config() throws Exception {
		ConfigurationBuilder builder = new ConfigurationBuilder();
		if (CONSUMER_KEY == null || CONSUMER_KEY.equals(""))
			throw new Exception("please insert your Consumer key");
		else
			builder.setOAuthConsumerKey(CONSUMER_KEY);
		if (CONSUMER_SECRET == null || CONSUMER_SECRET.equals(""))
			throw new Exception("please insert your Consumer secret key");
		else
			builder.setOAuthConsumerSecret(CONSUMER_SECRET);
		Configuration configuration = builder.build();
		TwitterFactory factory = new TwitterFactory(configuration);
		mTwitter = factory.getInstance();
		if (CALLBACK_URL == null || CALLBACK_URL.equals(""))
			throw new Exception("please insert your CALLBACK_URL");
		else
			mRequestToken = mTwitter.getOAuthRequestToken(CALLBACK_URL);
	}

	public void setCONSUMER_KEY(String cONSUMER_KEY) {
		CONSUMER_KEY = cONSUMER_KEY;
	}

	public String getOauthVerifier() {
		return mOauthVerifier;
	}

	public void setCONSUMER_SECRET(String cONSUMER_SECRET) {
		CONSUMER_SECRET = cONSUMER_SECRET;
	}

	public void setCALLBACK_URL(String cALLBACK_URL) {
		CALLBACK_URL = cALLBACK_URL;
	}

	WebViewClient webviewclient = new WebViewClient() {
		ProgressDialog progress;

		@Override
		public void onPageFinished(WebView view, String url) {
			super.onPageFinished(view, url);
			progress.dismiss();
		}

		@Override
		public void onPageStarted(WebView view, String url, Bitmap favicon) {
			super.onPageStarted(view, url, favicon);
			progress = new ProgressDialog(mActivity);
			progress.setMessage("Loading");
			progress.show();
		}

		public boolean shouldOverrideUrlLoading(WebView view, String url) {
			boolean result = true;
			TwitterLoginListener listener = mTwitterLoginListener;
			if (url != null && url.startsWith(CALLBACK_URL)) {
				Uri uri = Uri.parse(url);
				Log.v("", url);
				if (uri.getQueryParameter("denied") != null) {
					listener.onTwitterLoginFailed();
				} else {
					mOauthVerifier = uri.getQueryParameter("oauth_verifier");
					try {
						mAccessToken = mTwitter.getOAuthAccessToken(
								mRequestToken, mOauthVerifier);
						listener.onTwitterLoginComplete(mTwitter);
					} catch (TwitterException e) {
						e.printStackTrace();
						listener.onTwitterLoginFailed();
					}
				}
			} else {
				result = super.shouldOverrideUrlLoading(view, url);
			}
			mDialog.dismiss();
			return result;
		}
	};

	public AccessToken getAccessToken() {
		return mAccessToken;
	}

	private class postAsyntask extends AsyncTask<String, Void, Boolean> {
		ProgressDialog dialog;

		@Override
		protected Boolean doInBackground(String... params) {
			boolean result = true;
			try {
				mTwitter.updateStatus(params[0]);
			} catch (TwitterException e) {
				e.printStackTrace();
			}
			return result;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);

			TwitterPostStatusListener listener = mPostStatusListener;
			if (listener != null)
				if (result)
					listener.onPostStatusSuccess();
				else
					listener.onPostStatusFail();
			dialog.dismiss();
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			dialog = new ProgressDialog(mActivity);
			dialog.setMessage("Processing");
			dialog.show();
		}

	}

	public interface TwitterPostStatusListener {
		public void onPostStatusSuccess();

		public void onPostStatusFail();
	}

	public interface TwitterLoginListener {
		public void onTwitterLoginComplete(Twitter twitter);

		public void onTwitterLoginFailed();
	}

}
