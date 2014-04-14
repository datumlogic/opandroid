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
using System.Text.RegularExpressions;
namespace HopSampleApp
{
	class SocialMediaFeature
	{
		public string Expands_URL(string url)
		{
			string[] splits;
			string returns = "";
			if (url.Length > 0)
			{
				if (url.IndexOf("youtu", 0) > 0 || url.IndexOf("youtube", 0) > 0)
				{
					if (url.IndexOf("v=", 0) > 0)
						splits = url.Split('=');
					else
						splits = Regex.Split(url, "be/");

					if ((splits[1].Length > 0))
					{
						if (Regex.IsMatch(splits[1], "/feature/i"))
						{
							splits[1] = Regex.Replace(splits[1], "&feature", "", RegexOptions.IgnoreCase);
						}
						returns = "<div style='height: 200px;width:295px; overflow: hidden;'><iframe width='293px' height='200px'  scrolling='no' allowTransparency='true' src='http://www.youtube.com/embed/" + splits[1] + "' frameborder='0'></iframe></div>";
					}
				}
				else if (url.IndexOf("vimeo", 0) > 0)
				{
					splits = Regex.Split(url, "com/");
					returns = "<div style='height: 200px;width:295px; overflow: hidden;'><iframe src='http://player.vimeo.com/video/" + splits[1] + "?title=0&amp;byline=0&amp;portrait=0' width='293px' height='200px' scrolling='no' allowTransparency='true' frameborder='0'></iframe></div>";
				}
				//else
				  // returns = "<iframe src='" + url + "'?title=0&amp;byline=0&amp;portrait=0' width='250' height='200' frameborder='0'></iframe>";
			}
			return returns;
		}
	}
}

