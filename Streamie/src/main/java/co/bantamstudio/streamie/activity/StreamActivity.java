package co.bantamstudio.streamie.activity;
import co.bantamstudio.streamie.R;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Timer;
import java.util.TimerTask;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import co.bantamstudio.streamie.auth.model.Stream;
import org.springframework.util.Assert;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.view.MenuItem.OnMenuItemClickListener;

import co.bantamstudio.streamie.fragment.ChatFragment;
import co.bantamstudio.streamie.StreamieApplication;

import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.content.pm.ActivityInfo;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class StreamActivity extends SherlockFragmentActivity {

	private WebView mWebView;
	private FrameLayout mGestureArea;
	private LinearLayout mGestureAreaText;
	private FrameLayout mWebContainer;
	private MenuItem mLoadChat;
	private ProgressDialog mLoadingDialog;
	private StreamieApplication mApplication;
	private ChatFragment mChatFragment;
	private FragmentManager mFm;
	private boolean mIsPortrait = false;
	private String mJson;
	private Stream mStream;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// SET UP ORIENTATION
		mApplication = (StreamieApplication) getApplication();
		if (mApplication.getOrientation() == ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
			mIsPortrait = true;
			setContentView(R.layout.frame_layout);
		} else {
			setContentView(R.layout.frame_layout_video);
		}

		initialize();
	}

	private void initialize() {
		mFm = getSupportFragmentManager();

		mGestureAreaText = (LinearLayout) findViewById(R.id.gestureAreaText);

		// SET UP LONG CLICK REGION
		if (!mIsPortrait) {
			mGestureArea = (FrameLayout) findViewById(R.id.gestureArea);

			if (mGestureArea != null) {

				// HIDE INTRO TEXT
				mGestureAreaText.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// MAKE INVISIBLE ON CLICK
						if (mGestureAreaText.getVisibility() == View.VISIBLE) {
							mGestureAreaText.setVisibility(View.INVISIBLE);
							mApplication.setShowGestureArea(false);
						}
					}
				});

				// GRAB LONG CLICKS
				mGestureArea.setOnLongClickListener(new OnLongClickListener() {

					@Override
					public boolean onLongClick(View v) {
						loadChat();
						return true;
					}
				});

				mGestureArea.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {
						// HIDE CHAT IF AREA IS CLICKED
						if (mChatFragment != null && mChatFragment.isVisible()) {
							mFm.beginTransaction()
									.hide(mChatFragment).commit();
						}
					}
				});
			}
		}

		// SET UP LOADING DIALOG
		mLoadingDialog = new ProgressDialog(StreamActivity.this);
		mLoadingDialog.setCancelable(true);
		mLoadingDialog.setMessage("Loading, please wait");
		mLoadingDialog.show();
		// SHUT DOWN DIALOG AFTER 10 SECONDS IF OTHER CONDITION DOESN'T HAPPEN
		new Timer().schedule(new TimerTask() {
			@Override
			public void run() {
				if (mLoadingDialog != null && mLoadingDialog.isShowing()) {
					try {
						mLoadingDialog.dismiss();
						mLoadingDialog = null;
					} catch (Exception e) {
						// nothing
					}
				}
			}
		}, 30 * 1000);

		Intent intent = getIntent();
		mJson = intent.getStringExtra(Stream.SERIALIZED_JSON);
		String classString = intent.getStringExtra(Stream.TYPE);
		Assert.hasText(mJson);
		Assert.hasText(classString);

		Class streamClass;
        ObjectMapper mapper = new ObjectMapper();

		try {
			streamClass = Class.forName(classString);
            mStream = (Stream) mapper.readValue(mJson, streamClass);
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
			finish();
		} catch (JsonParseException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

		mWebContainer = (FrameLayout) findViewById(R.id.container);
		mWebView = new WebView(this);
		mWebView.setBackgroundColor(getResources().getColor(R.color.black));
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setPluginState(PluginState.ON);
		mWebView.getSettings().setSupportZoom(false);
		mWebView.getSettings().setLoadWithOverviewMode(true);
		if (Build.VERSION.SDK_INT >= VERSION_CODES.JELLY_BEAN) {
			mWebView.getSettings().setAllowUniversalAccessFromFileURLs(true);
		}
		// ALLOW FULLSCREEN FLASH ON API>14
		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public void onShowCustomView(View view, int requestedOrientation,
					WebChromeClient.CustomViewCallback callback) {
				super.onShowCustomView(view, callback);
				if (Build.VERSION.SDK_INT >= 14) {
					if (view instanceof FrameLayout) {
						mWebView.addView(view, new FrameLayout.LayoutParams(
								ViewGroup.LayoutParams.MATCH_PARENT,
								ViewGroup.LayoutParams.MATCH_PARENT,
								Gravity.CENTER));
						mWebView.setVisibility(View.VISIBLE);
					}
				}
			}
		});

		// IF HAS NEWER API, AND ADBLOCK IS ON, USE ADBLOCK WEBVIEWCLIENT
		if (Build.VERSION.SDK_INT >= 11 && mApplication.getAdBlock())
			mWebView.setWebViewClient(new FlashWebViewClient11());
		else
			mWebView.setWebViewClient(new FlashWebViewClient());

		if (mStream.getHls() != null) {
			mWebView.loadUrl(mStream.getHls());
		} else {
			// mWebView.loadDataWithBaseURL(JustinTVApi.JTV_BASE_URL_CDN,
			// mStream.getEmbed(), "text/html",
			// "UTF-8", JustinTVApi.JTV_BASE_URL_CDN);
			mWebView.loadData(mStream.getEmbed(), "text/html", "UTF-8");
		}

		// KEEP SCREEN ON
		this.getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN
						| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		mWebView.setKeepScreenOn(true);

		// ADD FLASH VIEW TO PARENT VIEW
		mWebContainer.addView(mWebView);

		// MAKE SURE OUR GESTURE VIEW IS ON TOP
		if (!mIsPortrait)
			mGestureArea.bringToFront();

		// SHOW DISCLAIMER
		if (mApplication.showVideoDisclaimer()) {

			// SHOW ALERT DIALOG WITH MESSAGE
			AlertDialog.Builder builder = new AlertDialog.Builder(this);

			builder.setMessage(R.string.video_disclaimer)
					.setPositiveButton(R.string.dialog_ok, null)
					.setNegativeButton(R.string.dialog_dont_show,
							new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									mApplication
											.showVideoDisclaimerAgain(false);
								}
							});

			builder.create().show();
		}

		// SHOW GESTURE AREA
		if (mGestureAreaText != null && mApplication.showGestureArea())
			mGestureAreaText.setVisibility(View.VISIBLE);

		// IMMEDIATELY LOAD CHAT IF PORTRAIT MODE
		if (mIsPortrait)
			loadChat();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// LOAD CHAT
		if (mApplication.getChat() && !mIsPortrait) {
			mLoadChat = menu.add("Show Chat");
			mLoadChat.setIcon(R.drawable.ic_menu_start_conversation);
			mLoadChat.setOnMenuItemClickListener(new OnMenuItemClickListener() {
				@Override
				public boolean onMenuItemClick(MenuItem item) {
					loadChat();
					return true;
				}
			});
		}
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		if (mWebView != null) {
			mWebView.loadUrl("about:blank");
		}
		super.onDestroy();
		if (mWebContainer != null)
			mWebContainer.removeAllViews();
		if (mWebView != null) {
			mWebView.destroy();
		}
	}

	@Override
	public void onBackPressed() {
		if (!mIsPortrait && mChatFragment != null && mChatFragment.isVisible()) {
			mFm.beginTransaction().hide(mChatFragment).commit();
		} else {
			AlertDialog.Builder alert = new AlertDialog.Builder(this);
			alert.setMessage(getResources().getString(R.string.leave_stream));
			alert.setPositiveButton(getResources().getString(R.string.yes),
					new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							StreamActivity.super.onBackPressed();
						}
					});
			alert.setNegativeButton(getResources().getString(R.string.no), null);
			alert.show();
		}
	}

	private void loadChat() {
		if (mChatFragment != null) {
			if (!mChatFragment.isVisible())
				mFm.beginTransaction().show(mChatFragment).commit();
		} else if (mStream.getChatEmbed() != null) {
			// LOAD FRAGMENT
			Bundle bdl = new Bundle();
			bdl.putString(StreamieApplication.KEY, mStream.getUsername());
			bdl.putString(StreamieApplication.CHAT_EMBED,
					mStream.getChatEmbed());
			if (mIsPortrait)
				bdl.putBoolean(StreamieApplication.PORTRAIT, true);
			mChatFragment = (ChatFragment) Fragment.instantiate(this,
					ChatFragment.class.getName(), bdl);
			mFm.beginTransaction().add(R.id.container, mChatFragment).commit();
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		this.callHiddenWebViewMethod("onPause");
	}

	@Override
	protected void onResume() {
		super.onResume();
		this.callHiddenWebViewMethod("onResume");
	}

	private void callHiddenWebViewMethod(String name) {
		if (mWebView != null) {
			try {
				Method method = WebView.class.getMethod(name);
				method.invoke(mWebView);
			} catch (NoSuchMethodException e) {
				Log.d("No such method: " + name, e.getMessage());
			} catch (IllegalAccessException e) {
				Log.d("Illegal Access: " + name, e.getMessage());
			} catch (InvocationTargetException e) {
				Log.d("Invocation Target Exception: " + name, e.getMessage());
			}
		}
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private class FlashWebViewClient11 extends FlashWebViewClient {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.webkit.WebViewClient#shouldInterceptRequest(android.webkit
		 * .WebView, java.lang.String)
		 */
		@Override
		public WebResourceResponse shouldInterceptRequest(WebView view,
				String url) {
			if (url.contains("quantserve") || url.contains("ImaAds.swf")
					|| url.contains("live_embed_click")
					|| url.contains("advertisements") || url.contains("Ads")
					|| url.contains("googleadservice")
					|| url.contains("liverail")
					|| url.contains("scorecardresearch")
					|| url.contains("googletagservices")
					|| url.contains("googlesynd") || url.contains("adwords")
					|| url.contains("adsense") || url.contains("liftdna"))
				return new WebResourceResponse("text", "utf-8", null);
			else
				return null;
		}
	}

	/**
	 * The Class FlashWebViewClient.
	 */
	private class FlashWebViewClient extends WebViewClient {

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * android.webkit.WebViewClient#onLoadResource(android.webkit.WebView,
		 * java.lang.String)
		 */
		@Override
		public void onLoadResource(WebView view, String url) {
			if (url.contains("usher.justin.tv/find") && mLoadingDialog != null
					&& mLoadingDialog.isShowing()) {
				try {
					mLoadingDialog.dismiss();
					mLoadingDialog = null;
				} catch (Exception e) {
					// nothing
				}
			}
		}
	}
}
