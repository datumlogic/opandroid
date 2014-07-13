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
using Android.Util;

namespace HopSampleApp
{
	/// <summary>
	/// #########################################################################
	///                    CSOPLOADLIBRARY
	/// #########################################################################
	/// CSOPLoadLibrary class contains methods:
	/// -------------------------------------------------------------------------
	/// - OPLoadLibrary
	/// - OPLoadLibrary(string your_so_library)
	/// -------------------------------------------------------------------------
	/// <remarks>
	/// This class has been designed for loading all application so library using
	/// Java.Lang.JavaSistem.LoadLibrary.
	/// It also has a method to load additional so library If you needed to load
	/// some addition so library.
	/// -------------------------------------------------------------------------
	/// NOTE:Method for loading addition so library are not tested to be aware is
	/// you run to some problem using it.
	/// </remarks>
	/// </summary>
	public class CSOPLoadLibrary
	{
		#region Load Addition so library
		public void OPLoadLibrary(string your_so_library)
		{
			try
			{
				if(your_so_library != null)
				{
					Log.Debug("LOAD ADDITION LIBRARY LOADED",your_so_library);
					//Loading
					Java.Lang.JavaSystem.LoadLibrary(your_so_library);
				}
			}
			catch(Exception Error)
			{
				//Error message
				Log.Error("Error Loading",String.Format("{0} Error Loading:{1}",your_so_library,Error.Message));
			}
		}
		#endregion

		#region Method for loading SO OP library

		public void OPLoadLibrary()
		{
			try
			{
				//Start message
				Log.Debug("OP *.so library Loading","Start loading z_shared and openpeer library...");

				#region z_shared library

				try
				{
					//Start message
					Log.Debug("Start","z_shared start loading....");

					//Loading
					Java.Lang.JavaSystem.LoadLibrary("z_shared");

					//End message
					Log.Debug("End","Library z_shared is loaded!");
				}
				catch(Exception error)
				{
					//Error message
					Log.Error("Error Loading",String.Format("z_share library is not loaded! Error:{0}",error.Message));
				}

				#endregion

				#region openpeer library

				try
				{
					//Start message
					Log.Debug("Start","openpeer start loading....");

					//Loading
					Java.Lang.JavaSystem.LoadLibrary("openpeer");

					//End message
					Log.Debug("End","Library openpeer is loaded");
				}
				catch(Exception error)
				{
					//Error message
					Log.Error("Error Loading",String.Format("openpeer library is not loaded! Error:{0}",error.Message));
				}

				#endregion


				//End message
				Log.Debug("OP *.so library Loading","All *.so library are loaded.");

			}
			catch(Exception error)
			{
				//Error message
				Log.Error("Error",String.Format("Error Loading z_shared and openpeer library",error.Message));
			}
		}
		#endregion

		#region Singleton pattern

		private static CSOPLoadLibrary instance;

		private CSOPLoadLibrary(){	}

		public static CSOPLoadLibrary SharedLoadLibrary()
		{
			if (instance == null)
				instance = new CSOPLoadLibrary();
			return instance;
		}

		#endregion
	}
}

