package com.hungnguyen.qrcodescanner.adapter;

import java.util.ArrayList;

import twitter4j.Twitter;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.qrcodescanner.R;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.Facebook.DialogListener;
import com.facebook.android.FacebookError;
import com.hungnguyen.qrcodescanner.activity.ResultActivity;
import com.hungnguyen.qrcodescanner.database.Database;
import com.hungnguyen.qrcodescanner.model.HistoryItemEnity;
import com.hungnguyen.qrcodescanner.model.HistoryItemObject;
import com.hungnguyen.qrcodescanner.model.HistorySectionItemObject;
import com.hungnguyen.qrcodescanner.ownerlibs.TwitterAPI;
import com.hungnguyen.qrcodescanner.ownerlibs.TwitterAPI.TwitterLoginListener;
import com.hungnguyen.qrcodescanner.ownerlibs.TwitterAPI.TwitterPostStatusListener;
import com.hungnguyen.qrcodescanner.utility.Constants;
import com.hungnguyen.qrcodescanner.utility.DeleteItemHistoryListener;
import com.hungnguyen.qrcodescanner.utility.Util;

public class HistoryListAdapter extends ArrayAdapter<HistoryItemEnity>
		implements Constants {
	private int IMAGE_SIZE;
	Activity mContext;
	LayoutInflater mInflater;
	DeleteItemHistoryListener mListener;

	public HistoryListAdapter(Activity context,
			ArrayList<HistoryItemEnity> list, DeleteItemHistoryListener listener) {
		super(context, 0, list);
		this.mContext = context;
		this.mInflater = LayoutInflater.from(context);
		this.mListener = listener;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final Holder holder;
		HistoryItemEnity item = getItem(position);
		if (convertView == null) {
			if (item != null) {
				if (item.isSection()) {
					holder = new Holder();
					convertView = mInflater.inflate(
							R.layout.item_historylistview_section, null);
					convertView.setOnClickListener(null);
					convertView.setOnLongClickListener(null);
					convertView.setLongClickable(false);
					convertView.setOnTouchListener(null);
					convertView.setEnabled(false);
					convertView.setFocusable(false);
					holder.tvTitle = (TextView) convertView
							.findViewById(R.id.history_listview_section_text);
					HistorySectionItemObject sectionItem = (HistorySectionItemObject) item;
					holder.tvTitle.setText("" + sectionItem.getTitle());
					convertView.setTag(holder);
				} else { // isSection
					convertView = mInflater.inflate(
							R.layout.item_historylistview_entry, null);
					holder = new Holder();

					holder.ibMessage = (ImageButton) convertView
							.findViewById(R.id.historylistview_entry_ib_message);
					holder.ibEmail = (ImageButton) convertView
							.findViewById(R.id.historylistview_entry_ib_email);
					holder.ibTwitter = (ImageButton) convertView
							.findViewById(R.id.historylistview_entry_ib_twitter);
					holder.ibFacebook = (ImageButton) convertView
							.findViewById(R.id.historylistview_entry_ib_facebook);
					holder.ibDelete = (ImageButton) convertView
							.findViewById(R.id.historylistview_entry_ib_delete);
					holder.tvTitle = (TextView) convertView
							.findViewById(R.id.historylistview_entry_tv_title);
					holder.tvId = (TextView) convertView
							.findViewById(R.id.historylistview_entry_tv_id);
					holder.relativeLayout = (RelativeLayout) convertView
							.findViewById(R.id.historylistview_entry_front);

					final HistoryItemObject entryItem = (HistoryItemObject) item;
					holder.ibMessage.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (Util.listSwipe.get(position).isLeft) {
								if (mContext
										.getPackageManager()
										.hasSystemFeature(
												PackageManager.FEATURE_TELEPHONY)) {
									Intent smsIntent = new Intent(
											Intent.ACTION_VIEW);
									smsIntent.setData(Uri.parse("smsto:"));
									smsIntent
											.setType("vnd.android-dir/mms-sms");
									smsIntent.putExtra("sms_body",
											entryItem.getTitle());
									mContext.startActivity(smsIntent);
								} else {
									Toast.makeText(mContext,
											"This device can not send sms",
											Toast.LENGTH_SHORT);
								}
							}
						}
					});
					holder.ibEmail.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (Util.listSwipe.get(position).isLeft) {
								Intent email = new Intent(Intent.ACTION_SEND);
								email.putExtra(
										Intent.EXTRA_SUBJECT,
										mContext.getResources().getString(
												R.string.share_mail_title));
								email.putExtra(Intent.EXTRA_TEXT,
										entryItem.getTitle());
								email.setType("message/rfc822");
								mContext.startActivity(Intent.createChooser(
										email, "Choose an Email client :"));
							}
						}
					});
					holder.ibTwitter.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (Util.listSwipe.get(position).isLeft) {
								final TwitterAPI twitterAPI = new TwitterAPI(
										mContext);
								twitterAPI
										.setCALLBACK_URL(TWITTER_CALLBACK_URL);
								twitterAPI
										.setCONSUMER_KEY(TWITTER_CONSUMER_KEY);
								twitterAPI
										.setCONSUMER_SECRET(TWITTER_CONSUMER_SECRET);
								if (!twitterAPI.isAlreadyLogin()) {

									twitterAPI
											.showDialogLogin(new TwitterLoginListener() {

												@Override
												public void onTwitterLoginFailed() {

												}

												@Override
												public void onTwitterLoginComplete(
														Twitter twitter) {
													twitterAPI.postStatus(
															entryItem
																	.getTitle(),
															null);
												}
											});
								} else {
									twitterAPI.postStatus(entryItem.getTitle(),
											new TwitterPostStatusListener() {

												@Override
												public void onPostStatusSuccess() {

												}

												@Override
												public void onPostStatusFail() {

												}
											});
								}

							}
						}
					});
					holder.ibFacebook.setOnClickListener(new OnClickListener() {

						@SuppressWarnings("deprecation")
						@Override
						public void onClick(View v) {
							if (Util.listSwipe.get(position).isLeft) {
								Facebook fb = new Facebook(FACEBOOK_APP_API);
								Bundle parameters = new Bundle();
								parameters.putString("link",
										"" + entryItem.getTitle());
								/*
								 * parameters.putString("caption","this is Caption"
								 * ); parameters
								 * .putString("decription","this is Dicription"
								 * ); parameters.putString("link",
								 * "http://www.google.com/"); parameters
								 * .putString( "picture",
								 * "http://4.bp.blogspot.com/-99S_TJiEvSQ/U6-559gN6sI/AAAAAAAAF5k/CQzEswdibW0/s1600/android-icon.png"
								 * );
								 */
								fb.dialog(mContext, "feed", parameters,
										new DialogListener() {

											@Override
											public void onFacebookError(
													final FacebookError e) {
											}

											@Override
											public void onError(
													final DialogError e) {

											}

											@Override
											public void onComplete(
													final Bundle values) {
											}

											@Override
											public void onCancel() {

											}
										});
							}
						}
					});
					holder.ibDelete.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							if (Util.listSwipe.get(position).isRight) {
								Database db = new Database(mContext);
								db.delete(entryItem.getId());
								DeleteItemHistoryListener listener = mListener;
								listener.onDeleteItemHistoryComplete();
							}
						}
					});
					if (entryItem.getTitle().length() < 30)
						holder.tvTitle.setText("" + entryItem.getTitle());
					else {
						String st = entryItem.getTitle().substring(0, 27);
						st = st + "...";
						holder.tvTitle.setText(st);
					}
					holder.tvId.setText("" + entryItem.getId());
					int screenHeight = mContext.getResources()
							.getDisplayMetrics().widthPixels;
					IMAGE_SIZE = (screenHeight * 10 / 100) + 5; // 10 %
					BitmapFactory.Options dimensions = new BitmapFactory.Options();
					dimensions.inJustDecodeBounds = false;
					Bitmap bmsms = BitmapFactory.decodeResource(
							mContext.getResources(), R.drawable.ic_btn_sms,
							dimensions);
					holder.ibMessage.setImageBitmap(Bitmap.createScaledBitmap(
							bmsms, IMAGE_SIZE, IMAGE_SIZE, true));
					Bitmap bmemail = BitmapFactory.decodeResource(
							mContext.getResources(), R.drawable.ic_btn_mail);
					holder.ibEmail.setImageBitmap(Bitmap.createScaledBitmap(
							bmemail, IMAGE_SIZE, IMAGE_SIZE, true));
					Bitmap bmTwitter = BitmapFactory.decodeResource(
							mContext.getResources(),
							R.drawable.ic_btn_twitter_fc);
					holder.ibTwitter.setImageBitmap(Bitmap.createScaledBitmap(
							bmTwitter, IMAGE_SIZE, IMAGE_SIZE, true));
					Bitmap bmFacebook = BitmapFactory.decodeResource(
							mContext.getResources(), R.drawable.ic_btn_fb_fc);
					holder.ibFacebook.setImageBitmap(Bitmap.createScaledBitmap(
							bmFacebook, IMAGE_SIZE, IMAGE_SIZE, true));
					Bitmap bmDelete = BitmapFactory.decodeResource(
							mContext.getResources(),
							R.drawable.ic_btn_delete_fc);
					holder.ibDelete.setImageBitmap(Bitmap.createScaledBitmap(
							bmDelete, IMAGE_SIZE, IMAGE_SIZE, true));

					// int imageHeight = bmsms.getHeight();
					FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) holder.relativeLayout
							.getLayoutParams();
					params.height = IMAGE_SIZE;
					holder.relativeLayout.setLayoutParams(params);
					convertView.setTag(holder);
				}
			}
		} else {
			holder = (Holder) convertView.getTag();
		}
		return convertView;
	}

	@Override
	public int getViewTypeCount() {
		if (isEmpty())
			return 1;
		return getCount();
	}

	@Override
	public int getCount() {
		return super.getCount();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.BaseAdapter#getItemViewType(int)
	 * 
	 * @return -1 if item type is Section (Header) : disable swipe in this row.
	 */
	@Override
	public int getItemViewType(int position) {
		HistoryItemEnity item = getItem(position);
		if (item.isSection())
			return -1;
		return position;
	}

	@Override
	public boolean isEmpty() {
		return getCount() == 0;
	}

	public class Holder {
		TextView tvId;
		TextView tvTitle;
		ImageButton ibMessage;
		ImageButton ibEmail;
		ImageButton ibTwitter;
		ImageButton ibFacebook;
		ImageButton ibDelete;
		RelativeLayout relativeLayout;
	}
}
