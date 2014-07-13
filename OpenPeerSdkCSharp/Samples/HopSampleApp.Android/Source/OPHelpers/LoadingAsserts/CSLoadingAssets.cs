/*
Copyright (c) 2014, hookflash Inc.
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
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Android.Util;
using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;

namespace HopSampleApp
{
	/// <summary>
	/// #########################################################################
	///                           LOADING ASSETS
	/// #########################################################################
	/// CSLoadingAsserts class contains methods:
	/// -------------------------------------------------------------------------
	/// - getPropertyFromAssert
	/// - InitializeAdditionalAssets
	/// -------------------------------------------------------------------------
	/// <remarks>
	///  This class can be used when you needed to load some addition assert
	///  file and get some property from it,in places where it is impossible
	///  to use already implemented classes or just you do not want
	///  to waste time for that.
	/// -------------------------------------------------------------------------
	/// NOTE:Do not forget to create a new assets file, but you can use old one
	/// </remarks>
	/// </summary>
	class CSLoadingAssets
	{
		Context mContext;
		String AssertProperty;

		Java.Util.Properties mProperties=new Java.Util.Properties();

		#region Get Property From Assets
		public String getPropertyFromAssert(string your_property,string if_your_propery_faild)
		{
			try
			{
				AssertProperty=mProperties.GetProperty (your_property);
				if (AssertProperty != null) 
				{
					Log.Debug ("[TEST ASSETS] YOUR ADDITION PROPERTY ARE LOADED", AssertProperty);
					return AssertProperty;
				}
				else
				{
					Log.Warn ("[TEST ASSETS FAIL]","YOUR ADDITION PROPERTY ARE LOADED MANUAL");
					return if_your_propery_faild;
				}
			}
			catch(Exception Error)
			{
				Log.Error ("ADDITION ASSETS PROPERTY ERROR","NO SETTINGS FIND");
				throw new Exception(Error.Message);
			}

		}
		#endregion

		#region Initialize additional Assets
		public void  InitializeAdditionalAssets(Context context,String assertFileLocation)
		{
			Log.Debug ("TEST ASSETS","ADDITION ASSETS SETTINGS ARE LOADED");
			mContext = context;

			System.IO.Stream inputStream;
			try
			{
				Log.Debug("TEST ASSETS","ADDITION ASSETS FILE ARE OPENED");
				inputStream=context.Assets.Open(assertFileLocation);

			}
			catch
			{
				Log.Error ("Error","Error open");
				return;
			}

			try
			{
				Log.Debug("TEST ASSETS","APP PROPERTIES ARE LOADED FROM ADDITIONAL ASSETS FILE");
				mProperties.Load(inputStream);
			} 
			catch
			{

				Log.Error ("Error","Error Load");
			}

		}
		#endregion

		#region Singleton pattern

		private static CSLoadingAssets instance;

		private CSLoadingAssets(){	}

		public static CSLoadingAssets SharedLoadingAssets()
		{
			if (instance == null)
				instance = new CSLoadingAssets();
			return instance;
		}

		#endregion
	}
}

