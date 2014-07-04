package com.hungnguyen.qrcodescanner.activity;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.example.qrcodescanner.R;
import com.hungnguyen.qrcodescanner.adapter.SlidingListAdapter;
import com.hungnguyen.qrcodescanner.fragment.HistoryFragment;
import com.hungnguyen.qrcodescanner.fragment.IntroduceFragment;
import com.hungnguyen.qrcodescanner.fragment.ScannerFragment;
import com.hungnguyen.qrcodescanner.fragment.SettingsFragment;
import com.hungnguyen.qrcodescanner.model.SlidingListItemObject;

@SuppressLint("NewApi")
public class MainActivity extends Activity implements OnItemClickListener {
	DrawerLayout mDrawerLayout;
	ListView mListSlidingMenu;
	ActionBarDrawerToggle mDrawerToggle;
	String[] titles = { "Scan", "History", "Settings", "About" };
	int mIndex = 0;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mDrawerLayout = (DrawerLayout) findViewById(R.id.map_drawer_layout);
		mListSlidingMenu = (ListView) findViewById(R.id.main_lv_sliding_menu);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);
		mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout,
				R.drawable.ic_drawer, R.string.app_name, R.string.app_name) {

			@Override
			public void onDrawerClosed(View drawerView) {
				invalidateOptionsMenu();
				setTitleBar(mIndex);
			}

			@Override
			public void onDrawerOpened(View drawerView) {
				invalidateOptionsMenu();
				setTitle("Menu");
			}

		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);
		ArrayList<SlidingListItemObject> list = new ArrayList<SlidingListItemObject>();
		list.add(new SlidingListItemObject(R.drawable.beatbrick));
		list.add(new SlidingListItemObject(R.drawable.beauty));
		list.add(new SlidingListItemObject(R.drawable.canny));
		list.add(new SlidingListItemObject(R.drawable.daigia));
		mListSlidingMenu.setAdapter(new SlidingListAdapter(
				getApplicationContext(), list));
		mListSlidingMenu.setOnItemClickListener(this);
		if (savedInstanceState == null) {
			showFragment(0);
			setTitleBar(0);
		}
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
		mDrawerLayout.closeDrawer(mListSlidingMenu);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		return super.onMenuItemSelected(featureId, item);
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mListSlidingMenu);
		menu.findItem(R.id.menu_setting).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sync the toggle state after onRestoreInstanceState has occurred.
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pass any configuration change to the drawer toggls
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		showFragment(position);
		setTitleBar(position);

	}

	private void setTitleBar(int index) {
		mIndex = index;
		switch (index) {
		case 0:
			setTitle(titles[index]);
			break;
		case 1:
			setTitle(titles[index]);
			break;
		case 2:
			setTitle(titles[index]);
			break;
		case 3:
			setTitle(titles[index]);
			break;
		}
	}

	@Override
	public void onBackPressed() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Warning !");
		builder.setMessage("Do you want to close QRCode Application");
		builder.setNegativeButton("NO", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.setPositiveButton("YES", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
				finish();
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

}
