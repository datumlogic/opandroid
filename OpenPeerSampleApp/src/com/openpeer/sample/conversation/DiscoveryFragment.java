package com.openpeer.sample.conversation;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.openpeer.sample.BaseFragment;
import com.openpeer.sample.R;

public class DiscoveryFragment extends BaseFragment {

	public static DiscoveryFragment newInstance() {
		return new DiscoveryFragment();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		return inflater.inflate(R.layout.fragment_discovery, null);
	}

}
