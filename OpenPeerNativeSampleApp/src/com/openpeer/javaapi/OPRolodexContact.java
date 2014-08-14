/*
 
 Copyright (c) 2014, SMB Phone Inc.
 All rights reserved.
 
 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:
 
 1. Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.
 
 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR
 ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 
 The views and conclusions contained in the software and documentation are those
 of the authors and should not be interpreted as representing official policies,
 either expressed or implied, of the FreeBSD Project.
 
 */

package com.openpeer.javaapi;

import java.util.List;

import android.util.Log;

public class OPRolodexContact {

	public static class OPAvatar {
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
