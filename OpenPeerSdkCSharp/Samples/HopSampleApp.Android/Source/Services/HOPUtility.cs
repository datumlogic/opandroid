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
using System.Collections;
using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;

namespace HopSampleApp
{
	class HOPUtility
	{
		static string getBaseIdentityURIFromURI(string identityURI)
		{
			string ret = "";
			ArrayList identityParts = identityURI.Split("/");
			if (identityParts.Count > 3)
			{
				int maxCount = identityParts.Count - 1;
				for (int i = 0; i < maxCount; i++)
				{
					ret = new StringBuilder (ret).Append (String.Format("{0}/",identityParts.IndexOf(i))).ToString ();
				}

			}

			return ret;
		}

		static string getContactIdFromURI(string identityURI)
		{

			{
				string ret = "";
				ArrayList identityParts = new ArrayList(identityURI.Split("/".ToCharArray()));
				if (identityParts.Count > 3)
				{
					int index = identityParts.Count - 1;
					ret = identityParts[index].ToString();
				}

				return ret;
			}
		}

		static bool isBaseIdentityURI(string identityURI)
		{
			bool ret = true;
			ArrayList identityParts =new ArrayList(identityURI.Split("/".ToCharArray()));
			if (identityParts.Count > 3)
			{
				int index = identityParts.Count - 1;

				ret= identityParts.IndexOf(index).ToString().Length ==0;

			}

			return ret;
		}

		static string hashString(string stringToHash)
		{
			string ret = null;
			if (stringToHash.Length > 0)
			{
				//need sdk
				// zsLib::String value = openpeer::services::IHelper::convertToHex(*openpeer::services::IHelper::hash([stringToHash UTF8String]),FALSE);
				//if (value.size() > 0) ret = NSString.stringWithUTF8String(value);

			}

			return ret;
		}

	}
}

