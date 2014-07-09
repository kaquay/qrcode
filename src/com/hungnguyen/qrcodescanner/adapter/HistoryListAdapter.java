package com.hungnguyen.qrcodescanner.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.qrcodescanner.R;
import com.hungnguyen.qrcodescanner.model.HistoryItemEnity;
import com.hungnguyen.qrcodescanner.model.HistoryItemObject;
import com.hungnguyen.qrcodescanner.model.HistorySectionItemObject;

public class HistoryListAdapter extends ArrayAdapter<HistoryItemEnity>
		implements OnClickListener {
	Activity mContext;
	ArrayList<HistoryItemEnity> mList;
	LayoutInflater mInflater;

	public HistoryListAdapter(Activity context, ArrayList<HistoryItemEnity> list) {
		super(context, 0, list);
		this.mContext = context;
		this.mList = list;
		this.mInflater = LayoutInflater.from(context);
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
					holder.tvTitle = (TextView) convertView
							.findViewById(R.id.history_listview_section_text);
					HistorySectionItemObject sectionItem = (HistorySectionItemObject) item;
					holder.tvTitle.setText("" + sectionItem.getTitle());
					convertView.setTag(holder);
				} else { // isSection
					convertView = mInflater.inflate(
							R.layout.item_historylistview_entry, null);
					holder = new Holder();
					holder.llShareLayout = (LinearLayout) convertView
							.findViewById(R.id.historylistview_entry_layout_sharebutton);
					holder.ibMessage = (ImageButton) convertView
							.findViewById(R.id.historylistview_entry_ib_message);
					holder.ibEmail = (ImageButton) convertView
							.findViewById(R.id.historylistview_entry_ib_email);
					holder.ibTwitter = (ImageButton) convertView
							.findViewById(R.id.historylistview_entry_ib_twitter);
					holder.ibFacebook = (ImageButton) convertView
							.findViewById(R.id.historylistview_entry_ib_facebook);
					holder.ibSlideLeft = (ImageButton) convertView
							.findViewById(R.id.historylistview_entry_ib_sliding_left);
					holder.ibSlideRight = (ImageButton) convertView
							.findViewById(R.id.historylistview_entry_ib_sliding_right);
					holder.ibDelete = (ImageButton) convertView
							.findViewById(R.id.historylistview_entry_ib_delete);
					holder.tvTitle = (TextView) convertView
							.findViewById(R.id.historylistview_entry_tv_name);
					holder.tvId = (TextView) convertView
							.findViewById(R.id.historylistview_entry_tv_id);
					final HistoryItemObject entryItem = (HistoryItemObject) item;
					holder.ibSlideLeft
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									boolean statusLeft = entryItem.isShowLeft();
									entryItem.setShowLeft(!statusLeft);
									if (entryItem.isShowLeft()) {
										holder.llShareLayout
												.setVisibility(View.VISIBLE);
									} else {
										holder.llShareLayout
												.setVisibility(View.GONE);
									}
								}
							});
					holder.ibSlideRight
							.setOnClickListener(new OnClickListener() {

								@Override
								public void onClick(View v) {
									boolean statusRight = entryItem
											.isShowRight();
									entryItem.setShowRight(!statusRight);
									if (entryItem.isShowRight()) {
										holder.ibDelete
												.setVisibility(View.VISIBLE);
									} else {
										holder.ibDelete
												.setVisibility(View.GONE);
									}
								}
							});
					holder.ibMessage.setOnClickListener(this);
					holder.ibEmail.setOnClickListener(this);
					holder.ibTwitter.setOnClickListener(this);
					holder.ibFacebook.setOnClickListener(this);
					holder.ibDelete.setOnClickListener(this);
					holder.tvTitle.setText("" + entryItem.getTitle());
					holder.tvId.setText("" + entryItem.getId());
					if (entryItem.isShowLeft()) {
						holder.llShareLayout.setVisibility(View.VISIBLE);
					} else {
						holder.llShareLayout.setVisibility(View.GONE);
					}
					if (entryItem.isShowRight()) {
						holder.ibDelete.setVisibility(View.VISIBLE);
					} else {
						holder.ibDelete.setVisibility(View.GONE);
					}
					convertView.setTag(holder);
				}
			}
		} else {
			holder = (Holder) convertView.getTag();
		}
		// if (item != null) {
		// if (item.isSection()) {
		// HistorySectionItemObject sectionItem = (HistorySectionItemObject)
		// item;
		// if (convertView == null) {
		// holder = new Holder();
		// convertView = mInflater.inflate(
		// R.layout.item_historylistview_section, null);
		// convertView.setOnClickListener(null);
		// convertView.setOnLongClickListener(null);
		// convertView.setLongClickable(false);
		// holder.tvTitle = (TextView) convertView
		// .findViewById(R.id.history_listview_section_text);
		//
		// convertView.setTag(holder);
		// } else {
		// holder = (Holder) convertView.getTag();
		// }
		// holder.tvTitle.setText("" + sectionItem.getTitle());
		// } else {
		// HistoryItemObject entryItem = (HistoryItemObject) item;
		// if (convertView == null) {
		// convertView = mInflater.inflate(
		// R.layout.item_historylistview_entry, null);
		// holder = new Holder();
		// holder.llShareLayout = (LinearLayout) convertView
		// .findViewById(R.id.historylistview_entry_layout_sharebutton);
		// holder.ibMessage = (ImageButton) convertView
		// .findViewById(R.id.historylistview_entry_ib_message);
		// holder.ibEmail = (ImageButton) convertView
		// .findViewById(R.id.historylistview_entry_ib_email);
		// holder.ibTwitter = (ImageButton) convertView
		// .findViewById(R.id.historylistview_entry_ib_twitter);
		// holder.ibFacebook = (ImageButton) convertView
		// .findViewById(R.id.historylistview_entry_ib_facebook);
		// holder.ibSlideLeft = (ImageButton) convertView
		// .findViewById(R.id.historylistview_entry_ib_sliding_left);
		// holder.ibSlideRight = (ImageButton) convertView
		// .findViewById(R.id.historylistview_entry_ib_sliding_right);
		// holder.ibDelete = (ImageButton) convertView
		// .findViewById(R.id.historylistview_entry_ib_delete);
		// holder.tvTitle = (TextView) convertView
		// .findViewById(R.id.historylistview_entry_tv_name);
		// holder.tvId = (TextView) convertView
		// .findViewById(R.id.historylistview_entry_tv_id);
		// holder.ibSlideLeft.setOnClickListener(this);
		// holder.ibSlideRight.setOnClickListener(this);
		// holder.ibMessage.setOnClickListener(this);
		// holder.ibEmail.setOnClickListener(this);
		// holder.ibTwitter.setOnClickListener(this);
		// holder.ibFacebook.setOnClickListener(this);
		// holder.ibDelete.setOnClickListener(this);
		// holder.tvTitle.setText("" + entryItem.getTitle());
		// holder.tvId.setText("" + entryItem.getId());
		// if (entryItem.isShowLeft()) {
		// holder.llShareLayout.setVisibility(View.VISIBLE);
		// } else {
		// holder.llShareLayout.setVisibility(View.GONE);
		// }
		// if (entryItem.isShowRight()) {
		// holder.ibDelete.setVisibility(View.VISIBLE);
		// } else {
		// holder.ibDelete.setVisibility(View.GONE);
		// }
		// convertView.setTag(holder);
		// } else {
		// holder = (Holder) convertView.getTag();
		// }
		// }
		// }
		return convertView;
	}

	@Override
	public void onClick(View v) {
		// Integer index = (Integer) v.getTag();
		// HistoryItemEnity item = mList.get(index);
		// HistoryItemObject entryItem = (HistoryItemObject) item;
		switch (v.getId()) {
		// case R.id.historylistview_entry_ib_sliding_left:
		// boolean statusLeft = entryItem.isShowLeft();
		// entryItem.setShowLeft(!statusLeft);
		case R.id.historylistview_entry_ib_message:
			shareViaMessage();
			break;
		case R.id.historylistview_entry_ib_email:
			shareViaEmail();
			break;
		case R.id.historylistview_entry_ib_twitter:
			shareViaTwitter();
			break;
		case R.id.historylistview_entry_ib_facebook:
			shareViewFacebook();
			break;
		// case R.id.historylistview_entry_ib_sliding_right:
		// boolean statusRight = entryItem.isShowRight();
		// entryItem.setShowRight(!statusRight);
		// break;
		case R.id.historylistview_entry_ib_delete:
			// Database db = new Database(mContext);
			// db.delete("" + entryItem.getId());
			break;

		}
	}

	private void shareViewFacebook() {

	}

	private void shareViaTwitter() {

	}

	private void shareViaEmail() {

	}

	private void shareViaMessage() {

	}

	@Override
	public int getViewTypeCount() {

		return getCount();
	}

	@Override
	public int getItemViewType(int position) {

		return position;
	}

	public class Holder {
		TextView tvTitle;
		TextView tvId;
		LinearLayout llShareLayout;
		ImageButton ibMessage;
		ImageButton ibEmail;
		ImageButton ibTwitter;
		ImageButton ibFacebook;
		RelativeLayout rlShow;
		ImageButton ibSlideLeft;
		TextView tvName;
		ImageButton ibSlideRight;
		ImageButton ibDelete;
	}
}
