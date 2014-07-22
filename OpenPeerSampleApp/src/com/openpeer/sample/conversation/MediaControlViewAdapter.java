package com.openpeer.sample.conversation;

import android.R;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class MediaControlViewAdapter extends BaseAdapter {

	boolean mHasVideo;
//	final static int resourceIdsAudio={R.drawable}

	@Override
	public int getCount() {
		return mHasVideo ? 4 : 3;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		if(mHasVideo){
			
		} else {
			
		}
		return null;
	}

}
