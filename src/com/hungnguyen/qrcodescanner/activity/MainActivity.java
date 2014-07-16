package com.hungnguyen.qrcodescanner.activity;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
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
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingActivity;

public class MainActivity extends SlidingActivity implements Constants,
		OnClickListener, ChangeFragmentListener {
	ImageButton mIbMenu;
	/*
	 * Have 4 Fragment with them name : Scan, History, Settings, About
	 */
	String[] titles = { "Scan", "History", "Settings", "About" };
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
		getFragmentManager().beginTransaction()
				.replace(R.id.frame_slidingmenu, new SlidingMenuFragment(this))
				.commit();
		/*
		 * Set Width of SlidingMenu : 30 per cents of Screen width
		 */
		int width = getResources().getDisplayMetrics().widthPixels * 70 / 100;
		getSlidingMenu().setBehindOffset(width);

		mTvTitle = (TextView) findViewById(R.id.main_tv_titlebar);
		mIbMenu = (ImageButton) findViewById(R.id.main_ib_menu);
		mIbShortcut = (ImageButton) findViewById(R.id.main_ib_shortcut);
		mBtDeleteAll = (Button) findViewById(R.id.main_history_bt_delete);
		mRlActionBar = (RelativeLayout) findViewById(R.id.main_rl_actionbar);

		/*
		 * set ActionBar height : 10 per cents of Screen height
		 */
		int height = getResources().getDisplayMetrics().heightPixels / 10;
		mRlActionBar.getLayoutParams().height = height;
		mRlActionBar.requestLayout();
		mIbShortcut.setOnClickListener(this);
		mIbMenu.setOnClickListener(this);
		mBtDeleteAll.setOnClickListener(this);
		getActionBar().setDisplayHomeAsUpEnabled(false);
		getActionBar().setHomeButtonEnabled(false);
		/*
		 * Open ScanFragment (0) when App start
		 */
		if (savedInstanceState == null) {
			setTitleBar(0);
			showFragment(0);
		}
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
			editor.putInt(SHARE_AUTO_CLOSE_URL, 0);
			editor.putString(SHARE_SHORTCUT, "");
			editor.commit();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void setTitleBar(int index) {
		mIndex = index;
		switch (index) {
		case 0:
			mTvTitle.setText("" + titles[index]);
			break;
		case 1:
			mTvTitle.setText("" + titles[index]);
			break;
		case 2:
			mTvTitle.setText("" + titles[index]);
			break;
		case 3:
			mTvTitle.setText("" + titles[index]);
			break;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onBackPressed() Double backPressed to close
	 * App.
	 */
	@Override
	public void onBackPressed() {
		if (back_pressed + 500 > System.currentTimeMillis())
			finish();
		else
			showToast("Press again to exit!");
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
				showToast("You must insert shortcut url first !");
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
		AlertDialog.Builder dialog = new AlertDialog.Builder(
				getApplicationContext());
		dialog.setTitle("Warning !");
		dialog.setMessage("Are you sure to delete all ?");
		dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				Database db = new Database(getApplicationContext());
				db.DeleteAllItem();
				dialog.dismiss();
			}
		});
		dialog.show();
	}

	/*
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
	}

	@Override
	public void onChangFragmentListener(int position) {
		showFragment(position);
		setTitleBar(position);
		toggle();
	}
}
