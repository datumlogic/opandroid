package com.openpeer.javaapi;

import java.util.List;

import android.util.Log;

public class OPRolodexContact {

	class OPAvatar {
		private String mName;
		private String mURL;
		private int mWidth;
		private int mHeight;
		
		OPAvatar()
		{
			
		}
		
		public String getName() {
			return mName;
		}
		public void setName(String mName) {
			this.mName = mName;
		}
		public String getURL() {
			return mURL;
		}
		public void setURL(String mURL) {
			this.mURL = mURL;
		}
		public int getWidth() {
			return mWidth;
		}
		public void setWidth(int mWidth) {
			this.mWidth = mWidth;
		}
		public int getHeight() {
			return mHeight;
		}
		public void setHeight(int mHeight) {
			this.mHeight = mHeight;
		}
		
	};
	
	public void printInfo()
	{
		Log.d("output", "Contact Name is: " + mName);
		Log.d("output", "Contact Identity URI is: " + mIdentityURI);
		Log.d("output", "Contact Identity provider is: " + mIdentityProvider);
		Log.d("output", "Contact Profile URL is: " + mProfileURL);
		Log.d("output", "Contact VProfileURL is: " + mVProfileURL);
		Log.d("output", "Contact Disposition is: " + mDisposition.toString());
		Log.d("output", "Contact Avatars count is: " + mAvatars.size());
		Log.d("output", "Avatars:");
		for (OPAvatar avatar : mAvatars)
		{
			Log.d("output", "Avatar name is: " + avatar.mName);
			Log.d("output", "Avatar URL is: " + avatar.mURL);
			Log.d("output", "Avatar height is: " + avatar.mHeight);
			Log.d("output", "Avatar width is: " + avatar.mWidth);
		}
	}
	
	enum Dispositions
    {
      Disposition_NA,
      Disposition_Update,
      Disposition_Remove;
      
      Dispositions ()
      {
      }

    };
    
    private Dispositions mDisposition;
    private String mIdentityURI;
    private String mIdentityProvider;

    private String mName;
    private String mProfileURL;
    private String mVProfileURL;

    private List<OPAvatar> mAvatars;

	public Dispositions getDisposition() {
		return mDisposition;
	}

	public void setDisposition(Dispositions mDisposition) {
		this.mDisposition = mDisposition;
	}

	public String getIdentityURI() {
		return mIdentityURI;
	}

	public void setIdentityURI(String mIdentityURI) {
		this.mIdentityURI = mIdentityURI;
	}

	public String getIdentityProvider() {
		return mIdentityProvider;
	}

	public void setIdentityProvider(String mIdentityProvider) {
		this.mIdentityProvider = mIdentityProvider;
	}

	public String getName() {
		return mName;
	}

	public void setName(String mName) {
		this.mName = mName;
	}

	public String getProfileURL() {
		return mProfileURL;
	}

	public void setProfileURL(String mProfileURL) {
		this.mProfileURL = mProfileURL;
	}

	public String getVProfileURL() {
		return mVProfileURL;
	}

	public void setVProfileURL(String mVProfileURL) {
		this.mVProfileURL = mVProfileURL;
	}

	public List<OPAvatar> getAvatars() {
		return mAvatars;
	}

	public void setAvatars(List<OPAvatar> mAvatars) {
		this.mAvatars = mAvatars;
	}
}
