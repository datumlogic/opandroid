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

namespace HopSampleApp
{
	public class PropertiesForWebClient
	{
		#region mInnerFrameUrl

		private string _mInnerFrameUrl;

		public string mInnerFrameUrl
		{
			get
			{
				return _mInnerFrameUrl;
			}
			set
			{
				_mInnerFrameUrl = value;
			}
		}
		#endregion

		#region mMessageForInnerFrame

		private string _mMessageForInnerFrame;

		public string mMessageForInnerFrame
		{
			get
			{
				return _mMessageForInnerFrame;
			}
			set
			{
				_mMessageForInnerFrame = value;
			}
		}
		#endregion

		#region mNamespaceGrantUrl

		private string _mNamespaceGrantUrl="";

		public string mNamespaceGrantUrl
		{
			get
			{
				return _mNamespaceGrantUrl;
			}
			set	
			{
				_mNamespaceGrantUrl = value;
			}
		}

		#endregion

		#region mInnerFrameLoaded

		private Boolean _mInnerFrameLoaded=false;

		public Boolean mInnerFrameLoaded
		{
			get
			{
				return _mInnerFrameLoaded;
			}
			set	
			{
				_mInnerFrameLoaded = value;
			}
		}

		#endregion

		#region mNamespaceGrantInnerFrameLoaded

		private Boolean _mNamespaceGrantInnerFrameLoaded=false;

		public Boolean mNamespaceGrantInnerFrameLoaded
		{
			get
			{
				return _mNamespaceGrantInnerFrameLoaded;
			}
			set
			{
				_mNamespaceGrantInnerFrameLoaded = value;
			}
		}

		#endregion

		#region mNamespaceGrantStarted

		private Boolean _mNamespaceGrantStarted=false;

		public Boolean mNamespaceGrantStarted
		{
			get
			{
				return _mNamespaceGrantStarted;
			}
			set
			{
				_mNamespaceGrantStarted = value;
			}
		}

		#endregion

		#region Singleton pattern

		private static PropertiesForWebClient instance;

		private PropertiesForWebClient(){	}

		public static PropertiesForWebClient SharedProperty()
		{
			if (instance == null)
				instance = new PropertiesForWebClient();
			return instance;
		}

		#endregion
	}
}

