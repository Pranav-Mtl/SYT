package com.syt.balram.syt;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.syt.balram.syt.BE.ChatPeopleBE;

import java.util.ArrayList;

public class ChatListAdapter extends ArrayAdapter<ChatPeopleBE> {

	private final Activity context;
	private final ArrayList<ChatPeopleBE> list;

	public ChatListAdapter(Activity context, ArrayList<ChatPeopleBE> list) {
		super(context, R.layout.list_layout, list);
		this.context = context;
		this.list = list;
	}

	static class ViewHolder {
		protected TextView text;
		protected TextView sent_or_received;
		protected LinearLayout chat_row_lin,chat_text_bg;
	}

	@Override
	public int getItemViewType(int position) {
		return position;
	}

	@Override
	public int getViewTypeCount() {
		System.out.println("SIZE : " + list.size());
		return list.size();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder viewHolder = null;
		if (convertView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			convertView = inflator.inflate(R.layout.chat_list_row, null);
			viewHolder = new ViewHolder();
			viewHolder.chat_row_lin = (LinearLayout) convertView
					.findViewById(R.id.chat_row_lin);
			viewHolder.chat_text_bg= (LinearLayout) convertView.findViewById(R.id.chat_text_bg);

			viewHolder.text = (TextView) convertView
					.findViewById(R.id.person_name);
			viewHolder.sent_or_received = (TextView) convertView
					.findViewById(R.id.sent_or_received);
			viewHolder.text.setTextColor(Color.BLACK);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		if (list != null) {
			ChatPeopleBE h = list.get(position);
			viewHolder.text.setText(h.getPERSON_CHAT_MESSAGE());
			viewHolder.sent_or_received.setText(h.getCHAT_DATE());
			if (h.getPERSON_CHAT_TO_FROM().equalsIgnoreCase("RECEIVED")) {
				viewHolder.chat_row_lin.setGravity(Gravity.RIGHT);
				viewHolder.chat_text_bg.setBackgroundResource(R.drawable.chat_recieved_bg);
				viewHolder.chat_text_bg.setPadding(8,8,8,8);
			}
		}

		return convertView;
	}
}
