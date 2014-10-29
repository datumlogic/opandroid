package com.openpeer.javaapi;

import android.text.format.Time;

public class OPPresenceResource {

	public String mID;                       // resources with same ID are alternative formats/dimensions of the same information
	public String mRelatedID;                // alternative resources related to an existing ID
	public String mType;                     // purpose of resource so remote party can know what to do with resource

	public String mFriendlyName;             // human readable friendly name

	public String mResourceURL;              // where to download resource
	public String mMimeType;                 // mime type of resource
	public int mSize;                  // size in bytes of resource; 0 = unkonwn;

	public int mWidth;                    // width in pixels if known; negative means unknown
	public int mHeight;                   // height in pixels if known; negative means unknown
	public Time mLength;                 // how long is audio/video

	public String mExternalLinkURL;          // external link to resource

	public String mEncoding;                 // if set, resource is encoded/encrypted using this algorithm/secret (use IEncryptor/IDecryptor)

	public boolean hasData(){
		if (!mID.isEmpty() ||
				!mRelatedID.isEmpty() ||
				!mType.isEmpty() ||
				!mFriendlyName.isEmpty() ||
				!mResourceURL.isEmpty() ||
				!mMimeType.isEmpty() ||
				(mSize > 0) ||
				(mWidth > 0) ||
				(mHeight > 0) ||
				!Time.isEpoch(mLength) ||
				!mExternalLinkURL.isEmpty() ||
				!mEncoding.isEmpty())
		{
			return true;
		}
		else
		{
			return false;
		}

	}
}
