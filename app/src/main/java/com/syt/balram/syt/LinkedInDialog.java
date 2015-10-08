package com.syt.balram.syt;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Picture;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.syt.constant.Constant;
import com.google.code.linkedinapi.client.LinkedInApiClientFactory;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthService;
import com.google.code.linkedinapi.client.oauth.LinkedInOAuthServiceFactory;
import com.google.code.linkedinapi.client.oauth.LinkedInRequestToken;

import java.util.ArrayList;
import java.util.List;

/**
 * Linkedin dialog
 * @author Vivek Kumar Srivastava
 */
public class LinkedInDialog extends Dialog
{
	private ProgressDialog progressDialog = null;

	public static LinkedInApiClientFactory factory;
	public static LinkedInOAuthService oAuthService;
	public static LinkedInRequestToken liToken;

	String autoToken,authoTokenSecret,authUrl;



	public static final String LINKEDIN_CONSUMER_KEY = Constant.CONSUMER_KEY;
	public static final String LINKEDIN_CONSUMER_SECRET = Constant.CONSUMER_SECRET;

	public static final String OAUTH_CALLBACK_SCHEME = "x-oauthflow-linkedin";
	public static final String OAUTH_CALLBACK_HOST = "litestcalback";
	public static final String OAUTH_CALLBACK_URL = OAUTH_CALLBACK_SCHEME + "://" + OAUTH_CALLBACK_HOST;

	/**
	 * Construct a new LinkedIn  dialog
	 * @param context activity {@link android.content.Context}
	 * @param progressDialog {@link android.app.ProgressDialog}
	 */
	public LinkedInDialog(Context context, ProgressDialog progressDialog)
	{
		super(context);
		this.progressDialog = progressDialog;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		requestWindowFeature(Window.FEATURE_NO_TITLE);//must call before super.
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_linked_in_dialog);

		setWebView();
	}

	/**
	 * set webview.
	 */
	private void setWebView()
	{
		/*LinkedInOAuthService oauthService = LinkedInOAuthServiceFactory.getInstance().createLinkedInOAuthService(Constant.CONSUMER_KEY, Constant.CONSUMER_SECRET);
		LinkedInDialog.factory = LinkedInApiClientFactory.newInstance(Constant.CONSUMER_KEY,Constant.CONSUMER_SECRET);
		LinkedInRequestToken requestToken = oauthService.getOAuthRequestToken(OAUTH_CALLBACK_URL);
		String autoToken = requestToken.getToken();
		String authoTokenSecret = requestToken.getTokenSecret();
		String authUrl = requestToken.getAuthorizationUrl();
		Log.i("Tok", autoToken);
		Log.i("Tok", authoTokenSecret);
		Log.i("Tok", authUrl);

		WebView mWebView = (WebView) findViewById(R.id.webkitWebView1);
		//mWebView.getSettings().setJavaScriptEnabled(true);
		progressDialog.dismiss();
		//Log.i("LinkedinSample", LinkedInDialog.liToken.getAuthorizationUrl());
		mWebView.loadUrl(authUrl);
		mWebView.setWebViewClient(new HelloWebViewClient());
*/
		new getMapTask().execute();

	}

	/**
	 * webview client for internal url loading
	 */
	class HelloWebViewClient extends WebViewClient
	{
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String url)
		{
			if(url.contains(OAUTH_CALLBACK_URL))
			{
				Uri uri = Uri.parse(url);
				String verifier = uri.getQueryParameter("oauth_verifier");
				Log.d("DialogVerifier",verifier);
				cancel();

				for(OnVerifyListener d : listeners)
				{
					//call listener method
					d.onVerify(verifier);
				}
			}
			else
			{
				Log.i("LinkedinSampleDialog", "url: "+url);
				view.loadUrl(url);
			}

			return true;
		}
	}

	/**
	 * List of listener.
	 */
	private List<OnVerifyListener> listeners = new ArrayList<OnVerifyListener>();

	/**
	 * Register a callback to be invoked when authentication  have finished.
	 *@param data The callback that will run
	 */
	public void setVerifierListener(OnVerifyListener data)
	{
		listeners.add(data);
	}

	/**
	 * Listener for oauth_verifier.
	 */
	interface OnVerifyListener
	{
		/**
		 * invoked when authentication  have finished.
		 * @param verifier oauth_verifier code.
		 */
		public void onVerify(String verifier);
	}

	class getMapTask extends AsyncTask<String, String, LinkedInRequestToken> {

		@Override
		protected LinkedInRequestToken doInBackground(String... params) {


			LinkedInDialog.oAuthService = LinkedInOAuthServiceFactory.getInstance().createLinkedInOAuthService(Constant.CONSUMER_KEY, Constant.CONSUMER_SECRET);
			LinkedInDialog.factory = LinkedInApiClientFactory.newInstance(Constant.CONSUMER_KEY,Constant.CONSUMER_SECRET);
			LinkedInDialog.liToken = LinkedInDialog.oAuthService.getOAuthRequestToken(OAUTH_CALLBACK_URL);
				return liToken;

		}

		@Override
		protected void onPostExecute(LinkedInRequestToken requestToken) {


			//LinkedInDialog.oAuthService = LinkedInOAuthServiceFactory.getInstance().createLinkedInOAuthService(LINKEDIN_CONSUMER_KEY, LINKEDIN_CONSUMER_SECRET);
			//LinkedInDialog.factory = LinkedInApiClientFactory.newInstance(LINKEDIN_CONSUMER_KEY, LINKEDIN_CONSUMER_SECRET);

			//LinkedInDialog.liToken = LinkedInDialog.oAuthService.getOAuthRequestToken(OAUTH_CALLBACK_URL);
			String autoToken = requestToken.getToken();
			String authoTokenSecret = requestToken.getTokenSecret();
			String authUrl = requestToken.getAuthorizationUrl();
			Log.i("Tok", autoToken);
			Log.i("Tok", authoTokenSecret);
			Log.i("Tok", authUrl);

			WebView mWebView = (WebView) findViewById(R.id.webkitWebView1);
			mWebView.getSettings().setJavaScriptEnabled(true);
			//progressDialog.dismiss();
			//Log.i("LinkedinSample", LinkedInDialog.liToken.getAuthorizationUrl());
			mWebView.loadUrl(authUrl);
			mWebView.setWebViewClient(new HelloWebViewClient());

			mWebView.setPictureListener(new WebView.PictureListener() {
				@Override
				public void onNewPicture(WebView view, Picture picture) {
					if (progressDialog != null && progressDialog.isShowing()) {
						progressDialog.dismiss();
					}

				}
			});


		}

	}
}
