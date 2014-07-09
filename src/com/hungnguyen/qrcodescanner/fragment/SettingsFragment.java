package com.hungnguyen.qrcodescanner.fragment;

import java.util.ArrayList;

import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;

import kankan.wheel.widget.ArrayWheelAdapter;
import kankan.wheel.widget.WheelView;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.app.Fragment;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
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
import com.hungnguyen.qrcodescanner.model.SettingListViewItemEntity;
import com.hungnguyen.qrcodescanner.model.SettingSectionItemObject;
import com.hungnguyen.qrcodescanner.model.SettingSwitchObject;
import com.hungnguyen.qrcodescanner.utility.Constants;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class SettingsFragment extends Fragment implements OnItemClickListener,
		Constants {
	ListView mListView;
	ArrayList<SettingItemEntity> mList;
	Toast mToast;

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
		mList.add(new SettingSectionItemObject("SCANNER"));
		SharedPreferences sp = getActivity()
				.getSharedPreferences(SHARE_NAME, 0);
		boolean swSound = sp.getBoolean(SHARE_SW_SOUND, true);
		mList.add(new SettingSwitchObject(SETTING_ITEM_SOUND, "Sound", swSound));
		boolean swAutoOpen = sp.getBoolean(SHARE_SW_AUTO_OPEN, true);
		mList.add(new SettingSwitchObject(SETTING_ITEM_AUTO_OPEN_URL,
				"Auto Open URL", swAutoOpen));
		String urlprofile = sp.getString(SHARE_URL_PROFILE, "");
		mList.add(new SettingChooseObject(SETTING_ITEM_URL_PROFILE,
				"URL Profile", urlprofile, true, false, 0));
		int autoClose = sp.getInt(SHARE_AUTO_CLOSE_URL, 0);
		String st = "";
		if (autoClose > 0) {
			st = autoClose + " seconds";
		} else
			st = "Never";
		mList.add(new SettingChooseObject(SETTING_ITEM_AUTO_CLOSE_URL,
				"Auto close URL view after", st, true, true, 0));
		String shortcut = sp.getString(SHARE_SHORTCUT, "");
		if (shortcut.trim().length() == 0) {
			mList.add(new SettingChooseObject(SETTING_ITEM_SHORTCUT,
					"Shortcut", "None", true, false, 0));
		} else {
			mList.add(new SettingChooseObject(SETTING_ITEM_SHORTCUT,
					"Shortcut", shortcut, true, false, 0));
		}
		mList.add(new SettingSectionItemObject("SHARE"));
		mList.add(new SettingChooseObject(SETTING_ITEM_MESSAGE, "Message", "",
				false, true, R.drawable.ic_setting_sms));
		mList.add(new SettingChooseObject(SETTING_ITEM_MAIL, "Mail", "", false,
				true, R.drawable.ic_setting_mail));
		mList.add(new SettingChooseObject(SETTING_ITEM_TWITTER, "Twitter", "",
				false, true, R.drawable.ic_setting_twitter));
		mList.add(new SettingChooseObject(SETTING_ITEM_FACEBOOK, "Facebook",
				"", false, true, R.drawable.ic_setting_fb));

		SettingListAdapter adapter = new SettingListAdapter(getActivity(),
				mList);
		mListView.setAdapter(adapter);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		SettingListViewItemEntity listviewItem = (SettingListViewItemEntity) mList
				.get(position);
		switch (listviewItem.getId()) {
		case SETTING_ITEM_URL_PROFILE:
			setUpURLProfile();
			break;
		case SETTING_ITEM_AUTO_CLOSE_URL:
			setUpAutoCloseURL();
			break;
		case SETTING_ITEM_MESSAGE:
			setUpShareMessage();
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
		// TODO
		try {
			// AccessToken a = new
			// AccessToken(TWITTER_ACCESS_TOKEN,TWITTER_SECRET_ACCESS_TOKEN);
			Twitter twitter = new TwitterFactory().getInstance();
			twitter.setOAuthConsumer(TWITTER_ACCESS_TOKEN,
					TWITTER_SECRET_ACCESS_TOKEN);
			// twitter.setOAuthAccessToken(a);
			twitter.updateStatus("POST TEST");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressWarnings("deprecation")
	private void setUpShareFacebook() {
		Facebook fb = new Facebook(FACEBOOK_APP_API);
		Bundle parameters = new Bundle();
		parameters.putString("message",
				getActivity().getResources()
						.getString(R.string.share_mail_body));
		parameters.putString("caption", "this is Caption");
		parameters.putString("decription", "this is Dicription");
		parameters.putString("link", "http://www.google.com/");
		parameters
				.putString(
						"picture",
						"http://4.bp.blogspot.com/-99S_TJiEvSQ/U6-559gN6sI/AAAAAAAAF5k/CQzEswdibW0/s1600/android-icon.png");
		fb.dialog(getActivity(), "feed", parameters, new DialogListener() {

			@Override
			public void onFacebookError(final FacebookError e) {
				showToast("Failed");
			}

			@Override
			public void onError(final DialogError e) {

			}

			@Override
			public void onComplete(final Bundle values) {
				showToast("Done");
			}

			@Override
			public void onCancel() {

			}
		});
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
				String st = "" + etValue.getText().toString().trim();
				if (st.equals(shortcutURL)) {

				} else {
					Editor editor = sp.edit();
					editor.putString(SHARE_SHORTCUT, st);
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
		dialog.setTitle(R.string.dialog_picker_title);
		ArrayList<String> listValue = new ArrayList<String>();
		for (int i = 5; i <= 90; i += 5) {
			listValue.add("" + i + " seconds");
		}
		listValue.add("Never");
		final String[] menuWheel = (String[]) listValue.toArray();
		final WheelView wheel = (WheelView) dialog
				.findViewById(R.id.dialog_picker_wheelview);
		wheel.setAdapter(new ArrayWheelAdapter<String>(menuWheel));
		wheel.setVisibleItems(2);
		wheel.setCurrentItem(0);
		int index = sp.getInt(SHARE_AUTO_CLOSE_URL, 0);
		if (index > 0) {
			wheel.setCurrentItem(index);
		} else {
			wheel.setCurrentItem(menuWheel.length - 1);
		}
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
				int index = wheel.getCurrentItem() + 1;
				if (index < menuWheel.length) {
					Editor editor = sp.edit();
					editor.putInt(SHARE_AUTO_CLOSE_URL, index);
					editor.commit();
				} else {
					Editor editor = sp.edit();
					editor.putInt(SHARE_AUTO_CLOSE_URL, 0);
					editor.commit();
				}
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
