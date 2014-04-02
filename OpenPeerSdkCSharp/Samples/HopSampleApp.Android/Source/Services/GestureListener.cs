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
using System.IO;
using System.Xml;
using System.Xml.Schema;
using Mono;

namespace HopSampleApp
{
	class GestureListener : GestureDetector.SimpleOnGestureListener,GestureDetector.IOnDoubleTapListener,GestureDetector.IOnGestureListener
	{
		//public event Action LeftEvent;
		//public event Action RightEvent;
		//private static int SWIPE_MAX_OFF_PATH = 250;
		//private static int SWIPE_MIN_DISTANCE = 120;
		//private static int SWIPE_THRESHOLD_VELOCITY = 200;

		public bool OnDown(MotionEvent e){ Console.WriteLine ("OnDown");return false;}

		public bool OnFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
		{
			try{ } catch {/* catch error */}return false;
		}

		public void OnLongPress(MotionEvent e) { Console.WriteLine ("OnLongPress");} 

		public bool OnScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY){	return false; }

		public void OnShowPress(MotionEvent e){ Console.WriteLine ("OnShowPress"); }

		public bool OnSingleTapUp(MotionEvent e){ Console.WriteLine ("OnSingleTapUp");return true;	}

		public bool onDoubleTapEvent(MotionEvent e)	{ Console.WriteLine ("OnDoubleTapEvent"); return false;}

		public bool onSingleTapConfirmed(MotionEvent e) { Console.WriteLine ("OnSingleTapConfirmed"); return false;}

		public GestureListener(){ }

		public bool OnDoubleTap(MotionEvent e)
		{
			 
			if (JSONParserProperty.GestureOption != true) {	
				Console.WriteLine ("Gesture is on");
				JSONParserProperty.GestureOption = true;
			} else if (JSONParserProperty.GestureOption != false) {
				JSONParserProperty.GestureOption = false;

				Console.WriteLine ("Gesture is off");
			}    
			Console.WriteLine ("Starting Gesture Layout.");

			return  true;
		}


	}
}

