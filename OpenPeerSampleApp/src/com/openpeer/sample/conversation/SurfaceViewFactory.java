package com.openpeer.sample.conversation;

import java.lang.ref.WeakReference;
import java.util.Hashtable;

import org.webrtc.videoengine.ViERenderer;

import android.content.Context;
import android.view.SurfaceView;

public class SurfaceViewFactory {
	private static Hashtable<String, SurfaceView> mRemoteViews = new Hashtable<String, SurfaceView>();
	private static WeakReference<SurfaceView> mLocalView = new WeakReference<SurfaceView>(null);

	public static SurfaceView getLocalView(Context context) {
		SurfaceView view = mLocalView.get();
		if (view == null) {
			view = ViERenderer.CreateLocalRenderer(context);
			mLocalView = new WeakReference<SurfaceView>(view);
		}
		return view;
	}

	public static SurfaceView getRemoteSurfaceView(Context context, String peerId) {
		SurfaceView view = mRemoteViews.get(peerId);
		if (view != null) {
			return view;
		} else {
			view = ViERenderer.CreateRenderer(context, true);
			mRemoteViews.put(peerId, view);
			return view;
		}
	}

}
