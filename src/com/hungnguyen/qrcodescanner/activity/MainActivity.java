package com.hungnguyen.qrcodescanner.activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrcodescanner.R;
import com.hungnguyen.qrcodescanner.database.Database;
import com.hungnguyen.qrcodescanner.fragment.HistoryFragment;
import com.hungnguyen.qrcodescanner.fragment.IntroduceFragment;
import com.hungnguyen.qrcodescanner.fragment.ScannerFragment;
import com.hungnguyen.qrcodescanner.fragment.SettingsFragment;
import com.hungnguyen.qrcodescanner.fragment.SlidingMenuFragment;
import com.hungnguyen.qrcodescanner.utility.ChangeFragmentListener;
import com.hungnguyen.qrcodescanner.utility.Constants;
import com.hungnguyen.qrcodescanner.utility.Util;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;

public class MainActivity extends SlidingActivity implements Constants,
		OnClickListener, ChangeFragmentListener {
	ImageButton mIbMenu;

	String[] titles;
	int mIndex = 0;
	long back_pressed;
	Toast mToast;
	TextView mTvTitle;
	ImageButton mIbShortcut;
	Button mBtDeleteAll;
	float lastTranslate = 0.0f;
	RelativeLayout mRlActionBar;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getActionBar().hide();
		setContentView(R.layout.activity_main);
		setBehindContentView(R.layout.menu);
		/*
		 * Set Width of SlidingMenu : 30 per cents of Screen width for Portrait
		 * or 10 per cents for Landscape
		 */
			int width = getResources().getDisplayMetrics().widthPixels * 70 / 100;
			getSlidingMenu().setBehindOffset(width);
		/*
		 * Have 4 Fragment with them name : Scan, History, Settings, About
		 * strings.xml
		 */
		titles = getResources().getStringArray(R.array.title);
		mTvTitle = (TextView) findViewById(R.id.main_tv_titlebar);
		mIbMenu = (ImageButton) findViewById(R.id.main_ib_menu);
		mIbShortcut = (ImageButton) findViewById(R.id.main_ib_shortcut);
		mBtDeleteAll = (Button) findViewById(R.id.main_history_bt_delete);
		mRlActionBar = (RelativeLayout) findViewById(R.id.main_rl_actionbar);

		// int height = getResources().getDisplayMetrics().heightPixels / 10;
		// mRlActionBar.getLayoutParams().height = height;
		// mRlActionBar.requestLayout();
		mIbShortcut.setOnClickListener(this);
		mIbMenu.setOnClickListener(this);
		mBtDeleteAll.setOnClickListener(this);
		getActionBar().setDisplayHomeAsUpEnabled(false);
		getActionBar().setHomeButtonEnabled(false);

		/*
		 * Config app when the first use.
		 */
		SharedPreferences sp = getSharedPreferences(SHARE_NAME, 0);
		boolean isFirst = sp.getBoolean(SHARE_IS_FIRST, true);
		if (isFirst) {
			Editor editor = sp.edit();
			editor.putBoolean(SHARE_IS_FIRST, false);
			editor.putBoolean(SHARE_SW_SOUND, true);
			editor.putBoolean(SHARE_SW_AUTO_OPEN, true);
			editor.putString(SHARE_URL_PROFILE, null);
			editor.putInt(SHARE_AUTO_CLOSE_URL, 18);
			editor.putString(SHARE_SHORTCUT, "");
			editor.commit();
		}
		/*
		 */
		if (android.os.Build.VERSION.SDK_INT > 9) {
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
					.permitAll().build();
			StrictMode.setThreadPolicy(policy);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Bitmap bmMenu = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_btn_menu);
		mIbMenu.setImageBitmap(Bitmap.createScaledBitmap(bmMenu, 55, 55, true));
		Bitmap bmFavorite = BitmapFactory.decodeResource(getResources(),
				R.drawable.ic_favourite_browser);
		mIbShortcut.setImageBitmap(Bitmap.createScaledBitmap(bmFavorite, 50,
				50, true));

		getFragmentManager().beginTransaction()
				.replace(R.id.frame_slidingmenu, new SlidingMenuFragment(this))
				.commit();
		setTitleBar(Util.index);
		showFragment(Util.index);
	}

	private void setTitleBar(int index) {
		mIndex = index;
		switch (index) {
		case 0:
			mTvTitle.setText(titles[index]);
			break;
		case 1:
			mTvTitle.setText(titles[index]);
			break;
		case 2:
			mTvTitle.setText(titles[index]);
			break;
		case 3:
			mTvTitle.setText(titles[index]);
			break;
		}
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onBackPressed() Double backPressed to close
	 *      App.
	 */
	@Override
	public void onBackPressed() {
		if (back_pressed + 500 > System.currentTimeMillis())
			finish();
		else
			showToast(getResources().getString(R.string.backpress_to_exit));
		back_pressed = System.currentTimeMillis();
	}

	private void showToast(final CharSequence msg) {
		if (mToast == null) {
			mToast = Toast.makeText(MainActivity.this, "", Toast.LENGTH_SHORT);
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

	/**
	 * 
	 */
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.main_ib_shortcut:
			SharedPreferences sp = getSharedPreferences(SHARE_NAME, 0);
			String url = sp.getString(SHARE_SHORTCUT, "");
			if (!url.equals("")) {
				Intent intent = new Intent(MainActivity.this,
						ResultActivity.class);
				Bundle extras = new Bundle();
				extras.putString("url", url);
				intent.putExtras(extras);
				startActivity(intent);
			} else {
				showToast(getResources().getString(R.string.shortcut_url_null));
			}
			break;
		case R.id.main_ib_menu:
			toggle();
			break;
		case R.id.main_history_bt_delete:
			DeleteAll();
			break;
		}
	}

	private void DeleteAll() {
		Database db = new Database(MainActivity.this);
		if (db.isHaveValues()) {
			AlertDialog.Builder dialog = new AlertDialog.Builder(
					MainActivity.this);
			dialog.setTitle(getResources().getString(
					R.string.dialog_title_warning));
			dialog.setMessage(getResources().getString(
					R.string.dialog_message_delete_all_value));
			dialog.setNegativeButton(getResources().getString(R.string.no),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
			dialog.setPositiveButton(getResources().getString(R.string.yes),
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Database db = new Database(getApplicationContext());
							db.DeleteAllItem();
							dialog.dismiss();
							showFragment(1);
						}
					});
			dialog.show();
		} else
			showToast(getResources().getString(R.string.null_database_value));
	}

	/**
	 * 
	 * @param index
	 */
	private void showFragment(int index) {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		Fragment fragment = null;
		switch (index) {
		case 0:
			fragment = new ScannerFragment();
			mBtDeleteAll.setVisibility(View.GONE);
			mIbShortcut.setVisibility(View.VISIBLE);
			break;
		case 1:
			fragment = new HistoryFragment();
			mBtDeleteAll.setVisibility(View.VISIBLE);
			mIbShortcut.setVisibility(View.GONE);
			break;
		case 2:
			fragment = new SettingsFragment();
			mBtDeleteAll.setVisibility(View.GONE);
			mIbShortcut.setVisibility(View.GONE);
			break;
		case 3:
			fragment = new IntroduceFragment();
			mBtDeleteAll.setVisibility(View.GONE);
			mIbShortcut.setVisibility(View.GONE);
			break;
		}
		fragmentTransaction.replace(R.id.main_frame_container, fragment)
				.commit();
		Util.index = index;
	}

	@Override
	public void onChangFragmentListener(int position) {
		if (mIndex != position) {
			showFragment(position);
			setTitleBar(position);
		}
		toggle();
	}
}
