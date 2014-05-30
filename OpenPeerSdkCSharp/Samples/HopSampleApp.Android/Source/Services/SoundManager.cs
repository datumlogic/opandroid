using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using Android.App;
using Android.Content;
using Android.OS;
using Android.Runtime;
using Android.Views;
using Android.Widget;

namespace HopSampleApp
{
	class SoundManager
	{
		private static SoundManager instance;
		private SoundManager()
		{ 
			/* code  */
		}
		public static SoundManager SharedSoundsManager()
		{
			if (instance == null)
				instance = new SoundManager();
			return instance;
		}
		public void StopCallingSound (){}
		public void StopRingingSound(){}

	}
}

