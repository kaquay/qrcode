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
import android.widget.TextView;

import com.example.qrcodescanner.R;
import com.hungnguyen.qrcodescanner.model.HistoryEntryItemObject;
import com.hungnguyen.qrcodescanner.model.HistoryItemObject;
import com.hungnguyen.qrcodescanner.model.HistorySectionItemObject;

public class HistoryListAdapter extends ArrayAdapter<HistoryItemObject> implements OnClickListener{
	Activity mContext;
	ArrayList<HistoryItemObject> mList;
	LayoutInflater mInflater;
	LinearLayout llShare;
	public HistoryListAdapter(Activity context, ArrayList<HistoryItemObject> list) {
		super(context,0,list);
		this.mContext = context;
		this.mList  = list;
		this.mInflater = LayoutInflater.from(context);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = convertView;
		HistoryItemObject item = mList.get(position);
		if (item!=null) {
			if (item.isSection()) {
				HistorySectionItemObject sectionItem = (HistorySectionItemObject) item;
				view = mInflater.inflate(R.layout.item_historylistview_section, null);
				view.setOnClickListener(null);
				view.setOnLongClickListener(null);
				view.setLongClickable(false);
				TextView sectionTvTitle = (TextView)view.findViewById(R.id.history_listview_section_text);
				sectionTvTitle.setText("" + sectionItem.getTitle());
			} else {
				HistoryEntryItemObject entryItem = (HistoryEntryItemObject)item;
				view = mInflater.inflate(R.layout.item_historylistview_entry, null);
				llShare = (LinearLayout)view.findViewById(R.id.historylistview_entry_layout_sharebutton);
				ImageButton ibMessage = (ImageButton)view.findViewById(R.id.historylistview_entry_ib_message);
				ImageButton ibEmail = (ImageButton)view.findViewById(R.id.historylistview_entry_ib_email);
				ImageButton ibTwitter = (ImageButton)view.findViewById(R.id.historylistview_entry_ib_twitter);
				ImageButton ibFacebook = (ImageButton)view.findViewById(R.id.historylistview_entry_ib_facebook);
				ImageButton ibLeftSliding = (ImageButton)view.findViewById(R.id.historylistview_entry_ib_sliding_left);
				ImageButton ibRightSliding= (ImageButton)view.findViewById(R.id.historylistview_entry_ib_sliding_right);
				ImageButton	ibDelete = (ImageButton)view.findViewById(R.id.historylistview_entry_ib_delete);
				TextView tvTitle = (TextView)view.findViewById(R.id.historylistview_entry_tv_name);
				TextView tvId =(TextView)view.findViewById(R.id.historylistview_entry_tv_id);
				ibMessage.setOnClickListener(this);
				ibEmail.setOnClickListener(this);
				ibTwitter.setOnClickListener(this);
				ibFacebook.setOnClickListener(this);
				ibLeftSliding.setOnClickListener(this);
				ibRightSliding.setOnClickListener(this);
				ibDelete.setOnClickListener(this);
				tvTitle.setText("" + entryItem.getTitle());
				tvId.setText("" + entryItem.getId());
			}
		}
		return view;
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.historylistview_entry_ib_sliding_left:
			llShare.setVisibility(View.VISIBLE);
			break;
		case R.id.historylistview_entry_ib_message:
			break;
		case R.id.historylistview_entry_ib_email:
			break;
		case R.id.historylistview_entry_ib_twitter:
			break;
		case R.id.historylistview_entry_ib_facebook:
			break;
		case R.id.historylistview_entry_ib_sliding_right:
			break;
		case R.id.historylistview_entry_ib_delete:
			break;
			
		}
	}
	
}
