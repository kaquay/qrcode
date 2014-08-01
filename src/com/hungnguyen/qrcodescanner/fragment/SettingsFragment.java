package com.hungnguyen.qrcodescanner.fragment;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import kankan.wheel.widget.ArrayWheelAdapter;
import kankan.wheel.widget.WheelView;
import twitter4j.Twitter;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrcodescanner.R;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.hungnguyen.qrcodescanner.adapter.SettingListAdapter;
import com.hungnguyen.qrcodescanner.model.SettingChooseObject;
import com.hungnguyen.qrcodescanner.model.SettingItemEntity;
import com.hungnguyen.qrcodescanner.model.SettingItemFactory;
import com.hungnguyen.qrcodescanner.model.SettingSectionItemObject;
import com.hungnguyen.qrcodescanner.model.SettingSwitchObject;
import com.hungnguyen.qrcodescanner.ownerlibs.TwitterAPI;
import com.hungnguyen.qrcodescanner.ownerlibs.TwitterAPI.TwitterLoginListener;
import com.hungnguyen.qrcodescanner.ownerlibs.TwitterAPI.TwitterPostStatusListener;
import com.hungnguyen.qrcodescanner.utility.Constants;
import com.hungnguyen.qrcodescanner.utility.Util;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SettingsFragment extends Fragment implements OnItemClickListener,
		Constants {
	ListView mListView;
	ArrayList<SettingItemEntity> mList;
	Toast mToast;
	Facebook mFacebobk;

	public SettingsFragment() {
		super();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater,
			@Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_settings, container,
				false);
		mListView = (ListView) view.findViewById(R.id.setting_list);
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		mList = new ArrayList<SettingItemEntity>();
		setupList();
		mListView.setOnItemClickListener(this);

	}

	private void setupList() {
		mList.clear();
		mList.add(new SettingSectionItemObject(getResources().getString(
				R.string.setting_section_scanner)));
		SharedPreferences sp = getActivity()
				.getSharedPreferences(SHARE_NAME, 0);
		boolean swSound = sp.getBoolean(SHARE_SW_SOUND, true);
		mList.add(new SettingSwitchObject(SETTING_ITEM_SOUND, getResources()
				.getString(R.string.setting_sound), swSound));
		boolean swAutoOpen = sp.getBoolean(SHARE_SW_AUTO_OPEN, true);
		mList.add(new SettingSwitchObject(SETTING_ITEM_AUTO_OPEN_URL,
				getResources().getString(R.string.setting_auto_open_url),
				swAutoOpen));
		String urlprofile = sp.getString(SHARE_URL_PROFILE, "");
		mList.add(new SettingChooseObject(SETTING_ITEM_URL_PROFILE,
				getResources().getString(R.string.setting_url_profile),
				urlprofile, true, false, 0));
		int autoClose = sp.getInt(SHARE_AUTO_CLOSE_URL, 0);
		String st = "";
		if (autoClose >= 0 && autoClose < 18) {
			st = (autoClose + 1) * 5 + " "
					+ getResources().getString(R.string.second);
		} else
			st = "Never";
		mList.add(new SettingChooseObject(SETTING_ITEM_AUTO_CLOSE_URL,
				getResources().getString(R.string.setting_auto_close_url), st,
				true, true, 0));
		String shortcut = sp.getString(SHARE_SHORTCUT, "");
		if (shortcut.trim().length() == 0) {
			mList.add(new SettingChooseObject(SETTING_ITEM_SHORTCUT,
					getResources().getString(R.string.setting_shortcut),
					"None", true, false, 0));
		} else {
			mList.add(new SettingChooseObject(SETTING_ITEM_SHORTCUT,
					getResources().getString(R.string.setting_shortcut),
					shortcut, true, false, 0));
		}
		mList.add(new SettingSectionItemObject(getResources().getString(
				R.string.setting_section_share)));

		mList.add(new SettingChooseObject(SETTING_ITEM_MESSAGE, getResources()
				.getString(R.string.setting_message), "", false, true,
				R.drawable.ic_setting_sms));

		mList.add(new SettingChooseObject(SETTING_ITEM_MAIL, getResources()
				.getString(R.string.setting_email), "", false, true,
				R.drawable.ic_setting_mail));
		mList.add(new SettingChooseObject(SETTING_ITEM_TWITTER, getResources()
				.getString(R.string.setting_twitter), "", false, true,
				R.drawable.ic_setting_twitter));
		mList.add(new SettingChooseObject(SETTING_ITEM_FACEBOOK, getResources()
				.getString(R.string.setting_facebook), "", false, true,
				R.drawable.ic_setting_fb));

		SettingListAdapter adapter = new SettingListAdapter(getActivity(),
				mList);
		mListView.setAdapter(adapter);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		SettingItemFactory listviewItem = (SettingItemFactory) mList
				.get(position);
		switch (listviewItem.getId()) {
		case SETTING_ITEM_URL_PROFILE:
			setUpURLProfile();
			break;
		case SETTING_ITEM_AUTO_CLOSE_URL:
			setUpAutoCloseURL();
			break;
		case SETTING_ITEM_MESSAGE:
			if (getActivity().getPackageManager().hasSystemFeature(
					PackageManager.FEATURE_TELEPHONY)) {
				setUpShareMessage();
			} else {
				showToast("This device can not send sms");
			}

			break;
		case SETTING_ITEM_MAIL:
			setUpShareEmail();
			break;
		case SETTING_ITEM_TWITTER:
			setUpShareTwitter();
			break;
		case SETTING_ITEM_FACEBOOK:
			setUpShareFacebook();
			break;
		case SETTING_ITEM_SHORTCUT:
			setUpShortcut();
			break;
		}
	}

	private void setUpShareTwitter() {
		final TwitterAPI twitterAPI = new TwitterAPI(getActivity());
		twitterAPI.setCALLBACK_URL(TWITTER_CALLBACK_URL);
		twitterAPI.setCONSUMER_KEY(TWITTER_CONSUMER_KEY);
		twitterAPI.setCONSUMER_SECRET(TWITTER_CONSUMER_SECRET);
		if (!twitterAPI.isAlreadyLogin()) {

			twitterAPI.showDialogLogin(new TwitterLoginListener() {

				@Override
				public void onTwitterLoginFailed() {

				}

				@Override
				public void onTwitterLoginComplete(Twitter twitter) {
					twitterAPI.postStatus(getActivity().getResources()
							.getString(R.string.sms_message), null);
				}
			});
		} else {
			twitterAPI.postStatus(
					getActivity().getResources()
							.getString(R.string.sms_message),
					new TwitterPostStatusListener() {

						@Override
						public void onPostStatusSuccess() {
							showToast("Done!");
						}

						@Override
						public void onPostStatusFail() {
							showToast("Failed!");
						}
					});
		}

	}

	@SuppressWarnings("deprecation")
	private void setUpShareFacebook() {
		mFacebobk = new Facebook(FACEBOOK_APP_API);
		mFacebobk.authorize(getActivity(), new String[] { "publish_stream",
				"read_stream", "offline_access" },
				new FacebookWallPostListener());
	}

	public class FacebookWallPostListener implements DialogListener {

		@Override
		public void onComplete(Bundle values) {
			new FacebookWallPost().execute();
		}

		@Override
		public void onFacebookError(FacebookError e) {

		}

		@Override
		public void onError(DialogError e) {

		}

		@Override
		public void onCancel() {

		}

	}

	private class FacebookWallPost extends AsyncTask<String, Void, Boolean> {
		ProgressDialog mDialog;

		@SuppressWarnings("deprecation")
		@Override
		protected Boolean doInBackground(String... params) {
			boolean result = true;
			try {
				Bundle parameters = new Bundle();
				parameters.putString("message", getActivity().getResources()
						.getString(R.string.share_mail_body));
				Bitmap bitmap = BitmapFactory.decodeResource(getResources(),
						R.drawable.logo);
				byte[] data = null;
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
				data = baos.toByteArray();
				if (data != null) {
					parameters.putByteArray("picture", data);
				}
				parameters
						.putString(Facebook.TOKEN, mFacebobk.getAccessToken());
				mFacebobk.request("me");
				mFacebobk.request("me/photos", parameters, "POST");

			} catch (Exception e) {
				result = false;
			}
			return result;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			mDialog.dismiss();
			if (result) {

			} else {

			}

		}

		@Override
		protected void onPreExecute() {
			mDialog = new ProgressDialog(getActivity());
			mDialog.setMessage("Processing");
			mDialog.show();
		}

	}

	private void setUpShareEmail() {
		Intent email = new Intent(Intent.ACTION_SEND);
		email.putExtra(Intent.EXTRA_SUBJECT, getActivity().getResources()
				.getString(R.string.share_mail_title));
		email.putExtra(Intent.EXTRA_TEXT, getActivity().getResources()
				.getString(R.string.share_mail_body));
		email.setType("message/rfc822");
		startActivity(Intent.createChooser(email, "Choose an Email client :"));
	}

	private void setUpShareMessage() {
		Intent smsIntent = new Intent(Intent.ACTION_VIEW);
		smsIntent.setData(Uri.parse("smsto:"));
		smsIntent.setType("vnd.android-dir/mms-sms");
		smsIntent.putExtra("sms_body",
				getActivity().getResources().getString(R.string.sms_message));
		startActivity(smsIntent);
	}

	private void setUpShortcut() {
		final Dialog dialog = new Dialog(getActivity());
		final SharedPreferences sp = getActivity().getSharedPreferences(
				SHARE_NAME, 0);
		dialog.setContentView(R.layout.dialog_insert_value);
		dialog.setTitle(getActivity().getResources().getString(
				R.string.dialog_insert_tv_title_shortcut));
		TextView tvDicription = (TextView) dialog
				.findViewById(R.id.dialog_insert_tv_decription);
		tvDicription.setText(""
				+ getActivity().getResources().getString(
						R.string.dialog_insert_tv_decription_shortcut));
		final EditText etValue = (EditText) dialog
				.findViewById(R.id.dialog_insert_et_value);
		final String shortcutURL = sp.getString(SHARE_SHORTCUT, "");
		etValue.setText("" + shortcutURL);
		Button btCancel = (Button) dialog
				.findViewById(R.id.dialog_insert_bt_cancel);
		Button btOk = (Button) dialog.findViewById(R.id.dialog_insert_bt_ok);
		btCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				dialog.dismiss();
			}
		});
		btOk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String url = "" + etValue.getText().toString().trim();
				if (!Util.isURL(url)) {

				} else {
					Editor editor = sp.edit();
					editor.putString(SHARE_SHORTCUT, url);
					editor.commit();
					showToast(getActivity().getResources().getString(
							R.string.toast_update_url_shortcut));
					setupList();
				}
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	private void setUpAutoCloseURL() {
		final SharedPreferences sp = getActivity().getSharedPreferences(
				SHARE_NAME, 0);
		final Dialog dialog = new Dialog(getActivity());
		dialog.setContentView(R.layout.dialog_picker);
		dialog.setTitle(R.string.dialog_picker_title);
		ArrayList<String> listValue = new ArrayList<String>();
		for (int i = 5; i <= 90; i += 5) {
			listValue.add("" + i + " s");
		}
		listValue.add("Never");
		final String[] menuWheel = listValue.toArray(new String[listValue
				.size()]);
		// final String[] menuWheel = { "1", "2", "3", "4" };
		final WheelView wheel = (WheelView) dialog
				.findViewById(R.id.dialog_picker_wheelview);
		ArrayWheelAdapter<String> adapter = new ArrayWheelAdapter<String>(
				getActivity(), menuWheel);
		wheel.setViewAdapter(adapter);
		wheel.setVisibleItems(5);
		wheel.setCurrentItem(0);
		int index = sp.getInt(SHARE_AUTO_CLOSE_URL, 0);
		// if (index > 0) {
		wheel.setCurrentItem(index);
		// } else {
		// wheel.setCurrentItem(menuWheel.length - 1);
		// }
		Button btSet = (Button) dialog.findViewById(R.id.dialog_picker_bt_set);
		Button btCancel = (Button) dialog
				.findViewById(R.id.dialog_picker_bt_cancel);
		btCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});
		btSet.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				int index = wheel.getCurrentItem();
				// if (index < menuWheel.length && index > 0) {
				Editor editor = sp.edit();
				editor.putInt(SHARE_AUTO_CLOSE_URL, index);
				editor.commit();
				// } else {
				// Editor editor = sp.edit();
				// editor.putInt(SHARE_AUTO_CLOSE_URL, 0);
				// editor.commit();
				// }
				setupList();
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	private void setUpURLProfile() {
		final Dialog dialog = new Dialog(getActivity());
		final SharedPreferences sp = getActivity().getSharedPreferences(
				SHARE_NAME, 0);
		dialog.setContentView(R.layout.dialog_insert_value);
		dialog.setTitle(getActivity().getResources().getString(
				R.string.dialog_insert_tv_title_urlprofile));
		TextView tvDicription = (TextView) dialog
				.findViewById(R.id.dialog_insert_tv_decription);
		tvDicription.setText(""
				+ getActivity().getResources().getString(
						R.string.dialog_insert_tv_decription_urlprofile));
		final EditText etValue = (EditText) dialog
				.findViewById(R.id.dialog_insert_et_value);
		final String urlProfile = sp.getString(SHARE_URL_PROFILE, "");
		etValue.setText("" + urlProfile);
		Button btCancel = (Button) dialog
				.findViewById(R.id.dialog_insert_bt_cancel);
		Button btOk = (Button) dialog.findViewById(R.id.dialog_insert_bt_ok);
		btCancel.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				dialog.dismiss();
			}
		});

		btOk.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				String st = "" + etValue.getText().toString().trim();
				if (st.equals(urlProfile)) {

				} else {
					Editor editor = sp.edit();
					editor.putString(SHARE_URL_PROFILE, st);
					editor.commit();
					showToast(getActivity().getResources().getString(
							R.string.toast_update_url_profile));
					setupList();
				}
				dialog.dismiss();
			}
		});

		dialog.show();
	}

	private void showToast(final CharSequence msg) {
		if (mToast == null) {
			mToast = Toast.makeText(getActivity().getApplicationContext(), "",
					Toast.LENGTH_SHORT);
		}
		if (mToast != null) {
			// Cancel last message toast
			if (mToast.getView().isShown()) {
				mToast.cancel();
			}
			mToast.setText(msg);
			mToast.show();
		}
	}
}
