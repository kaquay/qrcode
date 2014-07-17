package com.hungnguyen.qrcodescanner.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.example.qrcodescanner.R;
import com.facebook.android.DialogError;
import com.facebook.android.Facebook;
import com.facebook.android.FacebookError;
import com.facebook.android.Facebook.DialogListener;
import com.hungnguyen.qrcodescanner.activity.ResultActivity;
import com.hungnguyen.qrcodescanner.database.Database;
import com.hungnguyen.qrcodescanner.fragment.HistoryItemFragment;
import com.hungnguyen.qrcodescanner.model.HistoryItemEnity;
import com.hungnguyen.qrcodescanner.model.HistoryItemObject;
import com.hungnguyen.qrcodescanner.model.HistorySectionItemObject;
import com.hungnguyen.qrcodescanner.utility.Constants;
import com.hungnguyen.qrcodescanner.utility.DeleteItemHistoryListener;

public class HistoryListAdapter extends ArrayAdapter<HistoryItemEnity>
		implements Constants {
	Activity mContext;
	ArrayList<HistoryItemEnity> mList;
	LayoutInflater mInflater;
	DeleteItemHistoryListener mListener;
	public HistoryListAdapter(Activity context, ArrayList<HistoryItemEnity> list, DeleteItemHistoryListener listener) {
		super(context, 0, list);
		this.mContext = context;
		this.mList = list;
		this.mInflater = LayoutInflater.from(context);
		this.mListener = listener;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final Holder holder;
		HistoryItemEnity item = mList.get(position);
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
							Intent smsIntent = new Intent(Intent.ACTION_VIEW);
							smsIntent.setData(Uri.parse("smsto:"));
							smsIntent.setType("vnd.android-dir/mms-sms");
							smsIntent.putExtra("sms_body", entryItem.getTitle());
							mContext.startActivity(smsIntent);
						}
					});
					holder.ibEmail.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Intent email = new Intent(Intent.ACTION_SEND);
							email.putExtra(
									Intent.EXTRA_SUBJECT,
									mContext.getResources().getString(
											R.string.share_mail_title));
							email.putExtra(Intent.EXTRA_TEXT,
									entryItem.getTitle());
							email.setType("message/rfc822");
							mContext.startActivity(Intent.createChooser(email,
									"Choose an Email client :"));
						}
					});
					holder.ibTwitter.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							// TODO Auto-generated method stub

						}
					});
					holder.ibFacebook.setOnClickListener(new OnClickListener() {

						@SuppressWarnings("deprecation")
						@Override
						public void onClick(View v) {
							Facebook fb = new Facebook(FACEBOOK_APP_API);
							Bundle parameters = new Bundle();
							parameters.putString("link",
									"" + entryItem.getTitle());
							// parameters.putString("caption",
							// "this is Caption");
							// parameters.putString("decription",
							// "this is Dicription");
							// parameters.putString("link",
							// "http://www.google.com/");
							// parameters
							// .putString(
							// "picture",
							// "http://4.bp.blogspot.com/-99S_TJiEvSQ/U6-559gN6sI/AAAAAAAAF5k/CQzEswdibW0/s1600/android-icon.png");
							fb.dialog(mContext, "feed", parameters,
									new DialogListener() {

										@Override
										public void onFacebookError(
												final FacebookError e) {
										}

										@Override
										public void onError(final DialogError e) {

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
					});
					holder.ibDelete.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							Database db = new Database(mContext);
							db.delete("" + entryItem.getId());
							DeleteItemHistoryListener listener = mListener;
							listener.onDeleteItemHistoryComplete();
						}
					});
					holder.tvTitle.setText("" + entryItem.getTitle());
					holder.tvId.setText("" + entryItem.getId());
					holder.relativeLayout
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View arg0) {
									Intent intent = new Intent(mContext,
											ResultActivity.class);
									Bundle bundle = new Bundle();
									bundle.putString("url",
											entryItem.getTitle());
									intent.putExtras(bundle);
									mContext.startActivity(intent);
								}
							});

					int sreenHeight = mContext.getResources()
							.getDisplayMetrics().widthPixels;
					BitmapFactory.Options dimensions = new BitmapFactory.Options();
					dimensions.inJustDecodeBounds = false;
					Bitmap mBitmap = BitmapFactory.decodeResource(
							mContext.getResources(), R.drawable.ic_btn_sms_fc,
							dimensions);
					int imageHeight = dimensions.outHeight;

					FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) holder.relativeLayout
							.getLayoutParams();
					params.height = imageHeight;
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

		return getCount();
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
		HistoryItemEnity item = mList.get(position);
		if (item.isSection())
			return -1;
		return position;
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
