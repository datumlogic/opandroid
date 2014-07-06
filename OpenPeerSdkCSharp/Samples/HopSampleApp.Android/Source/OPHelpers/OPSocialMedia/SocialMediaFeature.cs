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
		#region Expands url

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

		#endregion

		#region Modern time stamp
		/* Social time stamp "less than 5 seconds" */
		public string Time_stamp(DateTime session_time)
		{
			DateTime StartTime = DateTime.Now;
			TimeSpan Time_Span = StartTime.Subtract (session_time);
			int seconds = Convert.ToInt32 (Time_Span.TotalSeconds);
			int minutes = seconds / 60;
			int hours = seconds / 3600;
			int days = seconds / 86400;
			int weeks = seconds / 604800;
			int months = seconds / 2419200;
			int years = seconds / 29030400;

			if (seconds <= 60)
			{
				return seconds + " seconds ago";
			}
			else if (minutes <= 60) 
			{
				if (minutes == 1)
				{
					return "one minute ago";
				}
				else
				{
					return minutes + " minutes ago";
				}
			} 
			else if (hours <= 24)
			{
				if (hours == 1)
				{
					return "one hour ago";
				}
				else
				{
					return hours + " hours ago";
				}
			}
			else if (days <= 7)
			{
				if (days == 1)
				{
					return "one day ago";
				}
				else
				{
					return days + " days ago";
				}
			}
			else if (weeks <= 4)
			{
				if (weeks == 1)
				{
					return "one week ago";
				}
				else
				{
					return weeks + " weeks ago";
				}
			}
			else if (months <= 12)
			{
				if (months == 1)
				{
					return "one month ago";
				}
				else
				{
					return months + " months ago";
				}
			}
			else
			{
				if (years == 1) {
					return "one year ago";
				} else 
				{
					return years + " years ago";
				}
			}
		}

		#endregion

		#region String to link
		/* String to link */
		public string StringToLink(string text)
		{
			text = " " + text;
			text = Regex.Replace(text, "(((f|ht){1}tp://)[-a-zA-Z0-9@:%_\\+.~#?&//=]+)", delegate(Match match)
			                     {
				string v = match.ToString();
				return "<a href='" + v + "' target='_blank' rel='nofollow'>" + v + "</a>";
			});

			text = Regex.Replace(text, "(((f|ht){1}tps://)[-a-zA-Z0-9@:%_\\+.~#?&//=]+)", delegate(Match match)
			                     {
				string v = match.ToString();
				return "<a href='" + v + "' target='_blank' rel='nofollow'>" + v + "</a>";
			});

			text = Regex.Replace(text, "[^(((f|ht){1}tp://)(((f|ht){1}tps://)](www.[-a-zA-Z0-9@:%_\\+.~#?&//=]+)", delegate(Match match)
			                     {
				string v = Regex.Match(match.ToString(), "(www.[-a-zA-Z0-9@:%_\\+.~#?&//=]+)").ToString();
				return "<a href='http://" + v + "' target='_blank' rel='nofollow'>" + v + "</a>";
			});

			text = Regex.Replace(text, "([_\\.0-9a-z-]+@([0-9a-z][0-9a-z-]+\\.)+[a-z]{2,4})", delegate(Match match)
			                     {
				string v = match.ToString();
				return "<a href='mailto:" + text + "' target='_blank' rel='nofollow'>" + v + "</a>";
			});
			return text;
		}

		#endregion

		#region text link
		/* Text link */
		public string TextLink(string text)
		{
			string a = "";
			if (Regex.IsMatch(text, pattern: "(((f|ht){1}tp://)[-a-zA-Z0-9@:%_\\+.~#?&//=]+)"))
			{
				a = Regex.Match(text, "(((f|ht){1}tp://)[-a-zA-Z0-9@:%_\\+.~#?&//=]+)").ToString();
			}
			else if (Regex.IsMatch(text, "(((f|ht){1}tps://)[-a-zA-Z0-9@:%_\\+.~#?&//=]+)"))
			{
				a = Regex.Match(text, "(((f|ht){1}tps://)[-a-zA-Z0-9@:%_\\+.~#?&//=]+)").ToString();
			}
			else
				a = "";
			return a;

		}

		#endregion

		#region html code
		/* html code */
		public string HtmlCode(string text)
		{
			string[] real = { "<", ">" };
			string[] replaced = { "&lt;", "&gt;" };
			string temp = text;
			for (int i = 0; i < 2; i++)
				text = Regex.Replace(text, real[i], replaced[i]);
			return text;
		}

		#endregion

		#region clear text

		public string Clear(string text)
		{
			string Result = System.Text.RegularExpressions.Regex.Replace(text, @"(\\)([\000\010\011\012\015\032\042\047\134\140])", "$2");
			return Result;
		}

		#endregion

	}
}

