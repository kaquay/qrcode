package com.hungnguyen.qrcodescanner.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrcodescanner.R;
import com.hungnguyen.qrcodescanner.adapter.SlidingListAdapter;
import com.hungnguyen.qrcodescanner.fragment.HistoryFragment;
import com.hungnguyen.qrcodescanner.fragment.IntroduceFragment;
import com.hungnguyen.qrcodescanner.fragment.ScannerFragment;
import com.hungnguyen.qrcodescanner.fragment.SettingsFragment;
import com.hungnguyen.qrcodescanner.model.SlidingListItemObject;
import com.hungnguyen.qrcodescanner.utility.Constants;
import com.hungnguyen.qrcodescanner.utility.MainLayout;

@SuppressLint("NewApi")
public class MainActivity extends Activity implements OnItemClickListener,
		Constants {
	ImageButton mIbMenu;
	ListView mListSlidingMenu;
	MainLayout mLayout;
	String[] titles = { "Scan", "History", "Settings", "About" };
	int mIndex = 0;
	long back_pressed;
	Toast mToast;
	TextView mTvTitle;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		getActionBar().hide();
		mLayout = (MainLayout) this.getLayoutInflater().inflate(
				R.layout.activity_main, null);
		setContentView(mLayout);
		mTvTitle = (TextView) findViewById(R.id.main_tv_titlebar);
		mIbMenu = (ImageButton) findViewById(R.id.main_ib_menu);
		mIbMenu.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mLayout.toggleMenu();
			}
		});
		mListSlidingMenu = (ListView) findViewById(R.id.main_lv_sliding_menu);
		getActionBar().setDisplayHomeAsUpEnabled(false);
		getActionBar().setHomeButtonEnabled(false);
		ArrayList<SlidingListItemObject> list = new ArrayList<SlidingListItemObject>();
		list.add(new SlidingListItemObject(R.drawable.ic_btn_scan));
		list.add(new SlidingListItemObject(R.drawable.ic_btn_history));
		list.add(new SlidingListItemObject(R.drawable.ic_btn_setting));
		list.add(new SlidingListItemObject(R.drawable.ic_btn_info));
		mListSlidingMenu.setAdapter(new SlidingListAdapter(
				getApplicationContext(), list));
		mListSlidingMenu.setOnItemClickListener(this);
		if (savedInstanceState == null) {
			showFragment(0);
			setTitleBar(0);
		}
		SharedPreferences sp = getSharedPreferences(SHARE_NAME, 0);
		boolean isFirst = sp.getBoolean(SHARE_IS_FIRST, true);
		if (isFirst) {
			Editor editor = sp.edit();
			editor.putBoolean(SHARE_IS_FIRST, false);
			editor.putBoolean(SHARE_SW_SOUND, true);
			editor.putBoolean(SHARE_SW_AUTO_OPEN, true);
			editor.putString(SHARE_URL_PROFILE, "");
			editor.putInt(SHARE_AUTO_CLOSE_URL, 0);
			editor.putString(SHARE_SHORTCUT, "");
			editor.commit();
		}
//		WindowManager wm = (WindowManager) this
//				.getSystemService(Context.WINDOW_SERVICE);
//		Display display = wm.getDefaultDisplay();
//		int height = display.getHeight();
//		int width = display.getWidth();
//		BitmapFactory.Options dimensions = new BitmapFactory.Options();
//		dimensions.inJustDecodeBounds = true;
//		Bitmap mBitmap = BitmapFactory.decodeResource(getResources(),
//				R.drawable.ic_bt_scan, dimensions);
//		int imageHeight = dimensions.outHeight;
//		int imageWidth = dimensions.outWidth;
//		int marginHoz = width - imageWidth / 2;
//		int marginVer = (height - (4 * imageHeight)) / 8;
//		mListSlidingMenu.setDividerHeight(marginVer);
//		mListSlidingMenu.setPadding(marginHoz, marginVer, marginHoz, marginVer);
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	private void showFragment(int index) {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		Fragment fragment = null;
		switch (index) {
		case 0:
			fragment = new ScannerFragment();
			break;
		case 1:
			fragment = new HistoryFragment();
			break;
		case 2:
			fragment = new SettingsFragment();
			break;
		case 3:
			fragment = new IntroduceFragment();
			break;
		}
		fragmentTransaction.replace(R.id.main_frame_container, fragment)
				.commit();
		// fragmentTransaction.addToBackStack(null);
		mListSlidingMenu.setItemChecked(index, true);
		mListSlidingMenu.setSelection(index);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_setting:
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		if (mIndex == position) {
			mLayout.toggleMenu();
			return;
		}
		showFragment(position);
		setTitleBar(position);
		mLayout.toggleMenu();
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

}
