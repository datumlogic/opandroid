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
	public interface ChatViewControllerDelegate
	{
		void prepareForKeyboardShowKeyboard(Dictionary<string,object> userInfo, bool showKeyboard);

	}
	public interface VideoCallViewControllerDelegate
	{
		void hideVideo(bool hide);

	}
	public interface LoginEventsDelegate 
	{
		void onStartLoginWithidentityURI();

		void onOpeningLoginPage();

		//void onLoginWebViewVisible(WebLoginViewController webLoginViewController);

		void onRelogin();

		void onLoginFinished();

		//void onIdentityLoginWebViewCloseForIdentityURI(WebLoginViewController webLoginViewController, string identityURI);

		void onIdentityLoginFinished();

		void onIdentityLoginError(string error);

		void onIdentityLoginShutdown();

		void onAccountLoginError(string error);

		//void onAccountLoginWebViewClose(WebLoginViewController webLoginViewController);

	}

	public  interface HTTPDownloaderDelegate
	{
		//void httpDownloaderDownloaded(HTTPDownloader downloader, string downloaded);

		//void httpDownloaderDidFailWithError(HTTPDownloader downloader, NSError error);

		//- (void) onSettingsDownloadCompletion:(NSDictionary*) inSettingsDictionary;
		//- (void) onSettingsDownloadFailure;
	}

	class Delegates	{}
}

