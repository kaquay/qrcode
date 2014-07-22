package com.hungnguyen.qrcodescanner.fragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import net.sourceforge.zbar.Config;
import net.sourceforge.zbar.Image;
import net.sourceforge.zbar.ImageScanner;
import net.sourceforge.zbar.Symbol;
import net.sourceforge.zbar.SymbolSet;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore.Audio.Media;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.dm.zbar.android.scanner.CameraPreview;
import com.example.qrcodescanner.R;
import com.hungnguyen.qrcodescanner.activity.ResultActivity;
import com.hungnguyen.qrcodescanner.database.Database;
import com.hungnguyen.qrcodescanner.utility.ChangeFragmentListener;
import com.hungnguyen.qrcodescanner.utility.Constants;
import com.hungnguyen.qrcodescanner.utility.Util;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class ScannerFragment extends Fragment implements
		Camera.PreviewCallback, Constants {
	Camera mCamera;
	CameraPreview mCameraPreview;
	ImageScanner mScanner;
	int[] mSymbols = { Symbol.CODE39, Symbol.QRCODE };
	Handler mAutoFocusHandler;
	boolean mPreviewing = true;
	FrameLayout mFrameLayout;
	ChangeFragmentListener mListener;
	static {
		System.loadLibrary("iconv");
	}

	public ScannerFragment() {
		super();
	}

	public void setupScanner() {
		mScanner = new ImageScanner();
		mScanner.setConfig(0, Config.X_DENSITY, 3);
		mScanner.setConfig(0, Config.Y_DENSITY, 3);

		if (mSymbols != null) {
			mScanner.setConfig(Symbol.NONE, Config.ENABLE, 0);
			for (int symbol : mSymbols) {
				mScanner.setConfig(symbol, Config.ENABLE, 1);
			}
		}
		Log.d("QRCODE", "setupScanner");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		Log.d("QRCODE", "onCreateView");
		View view = inflater.inflate(R.layout.fragment_scanner, container,
				false);

		mAutoFocusHandler = new Handler();

		mFrameLayout = (FrameLayout) view
				.findViewById(R.id.scanner_framelayout_camera);
		mCameraPreview = new CameraPreview(getActivity()
				.getApplicationContext(), this, autoFocusCB);
		mFrameLayout.addView(mCameraPreview);
		return view;
	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d("QRCODE", "onStart");
		if (!isCameraAvailable()) {
			showToast("Can't recognize Camera");
			return;
		}
		setupScanner();
	}

	public boolean isCameraAvailable() {
		PackageManager pm = getActivity().getPackageManager();
		return pm.hasSystemFeature(PackageManager.FEATURE_CAMERA);
	}

	@Override
	public void onResume() {
		super.onResume();
		try {
			// Open the default i.e. the first rear facing camera.
			mCamera = Camera.open();
			// mCamera.unlock();
			mCamera.setDisplayOrientation(90);
			if (mCamera == null) {
				// Cancel request if mCamera is null.
				cancelRequest();
				return;
			}

			mCameraPreview.setCamera(mCamera);
			mCameraPreview.showSurfaceView();
			Log.d("QRCODE", "bot of TRY");
			mPreviewing = true;
		} catch (Exception e) {
			Log.d("QRCODE", "CATCH");
			AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
			builder.setCancelable(false);
			builder.setTitle("Captix Scan");
			builder.setMessage("Sorry, the Android camera encountered a problem. You may need to restart the device");
			builder.setPositiveButton("OK", new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					getActivity().finish();
				}
			});
			AlertDialog dialog = builder.create();
			dialog.show();
		}
	}

	private void cancelRequest() {

	}

	@Override
	public void onPause() {
		super.onPause();
		try {
			// Because the Camera object is a shared resource, it's very
			// important to release it when the activity is paused.
			if (mCamera != null) {
				mCameraPreview.setCamera(null);
				mCamera.cancelAutoFocus();
				mCamera.setPreviewCallback(null);
				mCamera.stopPreview();
				mCamera.release();

				// According to Jason Kuang on
				// http://stackoverflow.com/questions/6519120/how-to-recover-camera-preview-from-sleep,
				// there might be surface recreation problems when the device
				// goes
				// to sleep. So lets just hide it and
				// recreate on resume
				mCameraPreview.hideSurfaceView();

				mPreviewing = false;
				mCamera = null;
			}
		} finally {

		}
	}

	private Runnable doAutoFocus = new Runnable() {
		public void run() {
			if (mCamera != null && mPreviewing) {
				mCamera.autoFocus(autoFocusCB);
			}
		}
	};

	// Mimic continuous auto-focusing
	Camera.AutoFocusCallback autoFocusCB = new Camera.AutoFocusCallback() {
		public void onAutoFocus(boolean success, Camera camera) {
			mAutoFocusHandler.postDelayed(doAutoFocus, 1000);
		}
	};

	private void showToast(String st) {
		Toast.makeText(getActivity(), st, Toast.LENGTH_SHORT).show();
	}

	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {

		Camera.Parameters parameters = camera.getParameters();
		Camera.Size size = parameters.getPreviewSize();

		Image barcode = new Image(size.width, size.height, "Y800");
		barcode.setData(data);

		int result = mScanner.scanImage(barcode);

		if (result != 0) {
			SharedPreferences sp = getActivity().getSharedPreferences(
					SHARE_NAME, 0);
			boolean isPlaySound = sp.getBoolean(SHARE_SW_SOUND, false);
			if (isPlaySound) {
				MediaPlayer player = MediaPlayer.create(getActivity(),
						R.drawable.camera_shutter);
				player.start();
			}
			mCamera.cancelAutoFocus();
			mCamera.setPreviewCallback(null);
			mCamera.stopPreview();
			mPreviewing = false;
			SymbolSet syms = mScanner.getResults();
			String symData = "";
			for (Symbol sym : syms) {
				symData = sym.getData();
				if (!TextUtils.isEmpty(symData)) {
					break;
				}
			}

			boolean autoOpenURL = sp.getBoolean(SHARE_SW_AUTO_OPEN, false);
			String URLProfile = sp.getString(SHARE_URL_PROFILE, "");
			if (URLProfile.contains("*var*")) {
				final String url = URLProfile.replace("*var*", symData);

				String today = now();
				Database db = new Database(getActivity());
				db.insert(url, today);
				if (!autoOpenURL) {
					AlertDialog.Builder builder = new AlertDialog.Builder(
							getActivity());
					builder.setTitle("Captix Scan");
					builder.setMessage("Would you like to connect to " + url);
					builder.setPositiveButton("YES",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									Intent intent = new Intent(getActivity(),
											ResultActivity.class);
									Bundle extras = new Bundle();
									extras.putString("url", url);
									intent.putExtras(extras);
									getActivity().startActivity(intent);
									dialog.dismiss();
								}
							});
					builder.setNegativeButton("NO", new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
							showScannerFragment();
						}
					});
					AlertDialog dialog = builder.create();
					dialog.show();

				} else {
					Intent intent = new Intent(getActivity(),
							ResultActivity.class);
					Bundle extras = new Bundle();
					extras.putString("url", url);
					intent.putExtras(extras);
					getActivity().startActivity(intent);
				}
			} else {
				if (Util.isURI(symData)) {
					Database db = new Database(getActivity());
					String today = now();
					db.insert(symData, today);

					if (!autoOpenURL) {
						final String url = symData;
						AlertDialog.Builder builder = new AlertDialog.Builder(
								getActivity());
						builder.setTitle("Captix Scan");
						builder.setMessage("Would you like to connect to "
								+ symData);
						builder.setPositiveButton("YES",
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										Intent intent = new Intent(
												getActivity(),
												ResultActivity.class);
										Bundle extras = new Bundle();
										extras.putString("url", url);
										intent.putExtras(extras);
										getActivity().startActivity(intent);
									}
								});
						builder.setNegativeButton("NO", new OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
								showScannerFragment();
							}
						});
						AlertDialog dialog = builder.create();
						dialog.show();
					} else {
						final String url = symData;
						Intent intent = new Intent(getActivity(),
								ResultActivity.class);
						Bundle extras = new Bundle();
						extras.putString("url", url);
						intent.putExtras(extras);
						getActivity().startActivity(intent);
					}

				} else {
					showToast(symData);
					showScannerFragment();
				}
			}
		}

	}

	private void showScannerFragment() {
		FragmentManager fragmentManager = getFragmentManager();
		FragmentTransaction fragmentTransaction = fragmentManager
				.beginTransaction();
		Fragment fragment = new ScannerFragment();
		fragmentTransaction.replace(R.id.main_frame_container, fragment)
				.commit();
	}

	private String now() {
		Date date = Calendar.getInstance().getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		return formatter.format(date);
	}

}
