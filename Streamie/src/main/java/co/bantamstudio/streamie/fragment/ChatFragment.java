package co.bantamstudio.streamie.fragment;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings.PluginState;
import android.webkit.WebSettings.ZoomDensity;
import android.webkit.WebView;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.actionbarsherlock.app.SherlockFragment;

import org.springframework.util.Assert;

import co.bantamstudio.streamie.R;
import co.bantamstudio.streamie.StreamieApplication;
import co.bantamstudio.streamie.ad.AdLoader;
import co.bantamstudio.streamie.ad.StreamieAd;
import co.bantamstudio.streamie.justintv.api.JustinTVApi;

public class ChatFragment extends SherlockFragment {

	private ChatWebView mChatView;
	private String mChannelName;
	private StreamieApplication mApplication;
	private FrameLayout mContainer;
	private boolean mIsPortrait;
	private String mChatEmbed;
	private float mAlpha;
    private StreamieAd mAds;
	
	private enum DIRECTION {
		LEFT,
		RIGHT
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
        return  inflater.inflate(R.layout.frame_layout_ad_chat, null);
	}
	
	@Override
	public void onStart() {
		super.onStart();
		
		mApplication = (StreamieApplication) getActivity().getApplication();
		
		// LOAD ADS
        mAds = AdLoader.initialize(getActivity());
        mAds.loadBanner((ViewGroup) getView().findViewById(R.id.ad_layout), null);
		
		mChannelName = getArguments().getString(StreamieApplication.KEY);
		mIsPortrait = getArguments().getBoolean(StreamieApplication.PORTRAIT, false);
		mChatEmbed = getArguments().getString(StreamieApplication.CHAT_EMBED);
		
		if (mChannelName == null || mChannelName.equalsIgnoreCase(""))
			getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
		
//		// SET ALPHA
//		int alphaInt = Integer.parseInt(mApplication.getChatAlpha());
//		String alphaCss = (alphaInt != 0)?"opacity: " + (float) alphaInt/100 + ";":"";
		
		// CHAT WIDTH
		DisplayMetrics dm = getActivity().getBaseContext().getResources().getDisplayMetrics();
		int widthInt = (int) (mApplication.getChatWidth() * 100);
		
		
		String css = JustinTVApi.CHAT_CSS_START + "iframe {border: none; width: " + widthInt + "% }" + JustinTVApi.CHAT_CSS_END;
		mChatEmbed = css + mChatEmbed;

		mContainer = (FrameLayout) getView().findViewById(R.id.container);

		if (mChatView == null) {
			mChatView = new ChatWebView(ChatFragment.this.getActivity());
			mChatView.setBackgroundColor(0x00000000);
			mChatView.getSettings().setJavaScriptEnabled(true);
			mChatView.getSettings().setPluginState(PluginState.ON);
			mChatView.getSettings().setSupportZoom(false);
			mChatView.getSettings().setLoadWithOverviewMode(true);
			mChatView.getSettings().setDefaultZoom(ZoomDensity.FAR);
			mChatView.getSettings().setUserAgentString(
					JustinTVApi.USER_AGENT_DESKTOP);
			mChatView.setWebChromeClient(new WebChromeClient());
			mChatView.loadData(mChatEmbed, "text/html", "UTF-8");
			mChatView.setBackgroundColor(getResources().getColor(R.color.transparent));

			
			// SET ALPHA ON > 11
			if (Build.VERSION.SDK_INT >= 11) {
				mAlpha = mApplication.getChatAlpha();
				mChatView.setAlpha(mAlpha);
			}
				
			mContainer.addView(mChatView);	
			
			if (!mIsPortrait) {
				FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(mChatView.getLayoutParams());
				int chatWidth, chatHeight;
				
				chatWidth = FrameLayout.LayoutParams.MATCH_PARENT;
				chatHeight = FrameLayout.LayoutParams.MATCH_PARENT;
				lp.width = chatWidth;
				lp.height = chatHeight;
				mChatView.setLayoutParams(lp);
			} else {
				RelativeLayout rl = (RelativeLayout) mContainer.getParent();
                Assert.notNull(rl);
				FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, dm.heightPixels/2, Gravity.BOTTOM);
				rl.setLayoutParams(lp);
			}

			
		}
		
//		RestClient chat = JtvApi.getChannelChatEmbed(mChannelName, null,null);
//		new ChatEmbedRequestAsync().execute(chat);
		
	}
	

	private boolean flingChat(DIRECTION dir) {
		switch (dir) {
		case LEFT:
			mChatView.loadUrl("javascript:(function() { " +  
	                "document.getElementsByTagName('iframe')[0].style.cssFloat = 'left'; " +  
	                "})()");  
			break;
		case RIGHT:
			mChatView.loadUrl("javascript:(function() { " +  
	                "document.getElementsByTagName('iframe')[0].style.cssFloat = 'right'; " +  
	                "})()");  
			break;
		default:
			return false;
		}

		return true;
	}

	private class ChatWebView extends WebView {
		
		/** The gd. */
        final GestureDetector gd;

		/**
		 * Instantiates a new chat web view.
		 *
		 * @param context the context
		 */
		public ChatWebView(Context context) {
			super(context);
			gd = new GestureDetector(context, ogl);
		}

		/* (non-Javadoc)
		 * @see android.webkit.WebView#onTouchEvent(android.view.MotionEvent)
		 */
		@Override
		public boolean onTouchEvent(MotionEvent event) {
			return (gd.onTouchEvent(event) || super.onTouchEvent(event));
		}

		/** The ogl. */
        final GestureDetector.SimpleOnGestureListener ogl = new GestureDetector.SimpleOnGestureListener() {
			// private static final int THRESHOLD = 5000;
			private static final int THRESHOLD = 2000;

			@Override
			public boolean onDown(MotionEvent event) {
				return false;
			}
			
			@Override
			public void onLongPress(MotionEvent e) {
				getActivity().getSupportFragmentManager().beginTransaction().hide(ChatFragment.this).commit();
			}

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
					float velocityX, float velocityY) {
				
				if (mIsPortrait)
					return false;
				
				if (velocityX > THRESHOLD) {
					// RIGHT FLING, MOVE CHAT WINDOW RIGHT
					return flingChat(DIRECTION.RIGHT);
				} else if (velocityX < -THRESHOLD) {
					// LEFT FLING, MOVE CHAT WINDOW LEFT
					return flingChat(DIRECTION.LEFT);
				}

				return true;
			}
		};
	}

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mAds != null)
            mAds.onStop();
    }

    //	private class ChatEmbedRequestAsync extends AsyncTask<RestClient, Integer, String> {
//
//		@Override
//		protected String doInBackground(RestClient... params) {
//			try {
//				params[0].Execute();
//				String response = params[0].getResponse();
//				return response;
//			} catch (Exception e) {
//				e.printStackTrace();
//				return null;
//			}
//		}
//
//		@Override
//		protected void onPostExecute(String result) {
//			if (result != null) {
////				result = result.replaceAll(JtvApi.CHAT_HEIGHT_REGEX_PATTERN,
////						JtvApi.CHAT_HEIGHT_REGEX_REPLACEMENT);
////				result = result.replaceAll(JtvApi.CHAT_WIDTH_REGEX_PATTERN,
////						JtvApi.CHAT_WIDTH_REGEX_REPLACEMENT);
//				
//				/* CHAT CSS SETUP */
//				
//				// SET ALPHA
//				int alphaInt = Integer.parseInt(mApplication.getChatAlpha());
//				String alphaCss = (alphaInt != 0)?"opacity: " + (float) alphaInt/100 + ";":"";
//				
//				String css = JtvApi.CHAT_CSS_START + "iframe {border: none;" + alphaCss + "}" + JtvApi.CHAT_CSS_END;
//				result = css + result;
//
//				mContainer = (FrameLayout) getView().findViewById(R.id.container);
//
//				if (mChatView == null) {
//					mChatView = new ChatWebView(ChatFragment.this.getActivity());
//					mChatView.setBackgroundColor(0x00000000);
//					mChatView.getSettings().setJavaScriptEnabled(true);
//					mChatView.getSettings().setPluginState(PluginState.ON);
//					mChatView.getSettings().setSupportZoom(false);
//					mChatView.getSettings().setLoadWithOverviewMode(true);
//					mChatView.getSettings().setDefaultZoom(ZoomDensity.FAR);
//					mChatView.getSettings().setUserAgentString(
//							JtvApi.USER_AGENT_DESKTOP);
//					mChatView.loadData(result, "text/html", "UTF-8");
//				
//					mContainer.addView(mChatView);
//
//					DisplayMetrics dm = new DisplayMetrics();
//					dm = getActivity().getBaseContext().getResources().getDisplayMetrics();
//					
//					if (!mIsPortrait) {
//						FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(mChatView.getLayoutParams());
//						int chatWidth, chatHeight;
//						
//						chatWidth = dm.widthPixels * Integer.parseInt(mApplication.getChatWidth()) / 100;
//						chatHeight = FrameLayout.LayoutParams.MATCH_PARENT;
//						lp.width = chatWidth;
//						lp.height = chatHeight;
//						mChatView.setLayoutParams(lp);
//					} else {
//						RelativeLayout rl = (RelativeLayout) mContainer.getParent();
//						FrameLayout.LayoutParams lp = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, dm.heightPixels/2, Gravity.BOTTOM);
//						rl.setLayoutParams(lp);
//					}
//
//					
//				}
//			} else {
//				// TOAST SAYING CAN'T GET CHAT INFO
//				Toast noChat = Toast.makeText(ChatFragment.this.getActivity(),getResources().getString(R.string.no_chat),
//						Toast.LENGTH_LONG);
//				noChat.show();
//				return;
//			}
//		}
//	}
}
