package co.bantamstudio.streamie;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.L;

import org.springframework.security.crypto.encrypt.AndroidEncryptors;
import org.springframework.social.connect.ConnectionRepository;
import org.springframework.social.connect.sqlite.SQLiteConnectionRepository;
import org.springframework.social.connect.sqlite.support.SQLiteConnectionRepositoryHelper;
import org.springframework.social.connect.support.ConnectionFactoryRegistry;
import org.springframework.util.Assert;
import org.springframework.util.Base64Utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import co.bantamstudio.streamie.auth.justintv.api.JustinTV;
import co.bantamstudio.streamie.auth.justintv.connect.JustinTVConnectionFactory;
import co.bantamstudio.streamie.auth.twitch.api.Twitch;
import co.bantamstudio.streamie.auth.twitch.connect.TwitchConnectionFactory;
import co.bantamstudio.streamie.fragment.StreamDetailFragment;
import co.bantamstudio.streamie.twitch.TwitchStreamDetailFragment;
import lombok.Getter;

public class StreamieApplication extends Application {
	// CONSTANTS
	private final static String APP_STRING = "co.bantamstudio.streamie";
	private final static String PREFS_MATURE_BOOLEAN = "pref_mature";
	private final static String PREFS_CHAT_ALPHA = "pref_chat_opacity";
	private final static String PREFS_JUSTIN_TV_ENABLED = "pref_justin_tv_enabled";
	private final static String PREFS_GENERAL_FLASH_BROWSER = "pref_alternative_flash";
    private final static String PREFS_GENERAL_HLS = "pref_alternative_hls";
	private final static String PREFS_TWITCH_ENABLED = "pref_twitch_enabled";
	private final static String PREFS_LANGUAGE = "pref_justin_tv_language";
	private final static String PREFS_ORIENTATION = "pref_orientation";
	private final static String PREFS_THEME = "pref_theme";
	private final static String PREFS_CHAT = "pref_chat_enabled";
	private final static String PREFS_CHAT_AUTOSCROLL = "prefs_chat_autoscroll";
	private final static String PREFS_OFFLINE_FAV = "prefs_offline_fav";
	private final static String PREFS_CHAT_WIDTH = "pref_chat_width";
	private final static String PREFS_STREAM_IMAGE = "pref_stream_image";
	private final static String PREFS_ADBLOCK_BOOLEAN = "pref_adblock";

    // OAUTH KEYS
	// The keys and secrets are base64 encoded and split up to help obfuscate the keys
	// ...pretty primitive, there are better approaches
    private String TwitchKey0 = "";
    private String TwitchKey1 = "";
    private String TwitchSecret0 = "";
    private String TwitchSecret1 = "";
    private String TwitchSecret2 = "";
    private String TwitchSecret3 = "";
    private String TwitchSecret4 = "";
    private String TwitchSecret5 = "";
    private String JustinTVKey0 = "";
    private String JustinTVKey1 = "";
    private String JustinTVSecret0 = "";
    private String JustinTVSecret1 = "";
    private String JustinTVSecret2 = "";
    private String JustinTVSecret3 = "";
    private String JustinTVSecret4 = "";
    private String ConnRepoPass0 = "";
    private String ConnRepoPass1 = "";
    private String ConnRepoPass2 = "";
    private String ConnRepoPass3 = "";
    private String ConnRepoSalt0 = "";
    private String ConnRepoSalt1 = "";
    private String ConnRepoSalt2 = "";
    private String ConnRepoSalt3 = "";
    private String ConnRepoSalt4 = "";
    private String ConnRepoSalt5 = "";
    private String ConnRepoSalt6 = "";


    public final static String STREAMIE_USER_AGENT = "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:6.0) Gecko/20100101 Firefox/6.0";

	private final static String STUDIO_NAME = "Bantam Studio";
	protected final static String APP_LINK = "<a href=\"http://bantamstudio.co/jtv\">"
			+ STUDIO_NAME + "</a>";
//	protected static final String FEEDBACK_LINK = "http://bantamstudio.co/streamie/feedback";
	private static final String FEEDBACK_INTENT_EMAIL = "streamie@bantamstudio.co";
	private static final String FEEDBACK_INTENT_BODY = "Please enter feedback here:\n\n\n\n\nTroubleshooting Information:\n\n";
//	protected static final String FEEDBACK_INTENT_TYPE = "message/rfc822";

	public static void sendFeedback(Context context) {
		Intent feedBackIntent = new Intent(Intent.ACTION_VIEW);
		Uri data = Uri.parse("mailto:" + FEEDBACK_INTENT_EMAIL + "?subject="
				+ context.getResources().getString(R.string.app_name)
				+ " Feedback" + "&body=" + FEEDBACK_INTENT_BODY
				+ context.getResources().getString(R.string.app_name)
				+ "\nModel: " + Build.MODEL + "\nDevice: " + Build.DEVICE
				+ "\nProduct: " + Build.PRODUCT + "\nManuf: "
				+ Build.MANUFACTURER + "\n" + context.getClass().toString());
		feedBackIntent.setData(data);
		context.startActivity(Intent.createChooser(feedBackIntent,
				"Send Feedback about Streamie"));
	}

	public static final String FRAGMENT = "launch_frag";
	protected final static String FLASH_MARKET_LINK = "market://details?id=com.adobe.flashplayer";
	public final static String FLASH_MARKET_LINK_HTTP = "https://play.google.com/store/apps/details?id=com.adobe.flashplayer";
	public final static String FLASH_TUTORIAL_VIDEO = "http://www.youtube.com/watch?v=iLjLbm_nBU8";
	public final static String FLASH_APK_ARCHIVE = "http://helpx.adobe.com/flash-player/kb/archived-flash-player-versions.html";
	private final static String FLASH_APK_LATEST_4 = "http://download.macromedia.com/pub/flashplayer/installers/archive/android/11.1.115.81/install_flash_player_ics.apk";
	private final static String FLASH_APK_LATEST_2_3 = "http://download.macromedia.com/pub/flashplayer/installers/archive/android/11.1.111.73/install_flash_player_pre_ics.apk";
//	public final static String STREAMIE_PRO_LINK = "https://play.google.com/store/apps/details?id=co.bantamstudio.streamie";

	public static final String KEY = "key";
	public static final String TITLE = "title";
    public static final String SUBTITLE = "subtitle";
	public static final String CHAT_EMBED = "chat_embed";

	protected static final String JSON_STRING = APP_STRING + "JSON_STRING";

	/** The Constant RTMP_ADDRESS. */
	public static final String RTMP_ADDRESS = "127.0.0.1";
	private static final String SHOW_VIDEO_DISCLAIMER = "show_video_disclaimer_" + 3;
	private static final String SHOW_GESTURE = "show_gesture";
	private static final String SHOW_RELEASE_NOTES = "show_release_notes";

	private static final String ALL = "all";
	public static final String PORTRAIT = "isPortrait";

	// // PROCESS FIELDS
	// private Process mStreamProcess;
	// private int mPLow = 32768;
	// private int mPHigh = 61000;
	// private int mStreamPort;
	// public int getStreamPort() {
	// return mStreamPort;
	// }

	// private LogStreamReader mLogStreamReader;
	// private Thread mStreamOutputThread;

	@Getter private static ImageLoader imageLoader;

	@Getter private static DisplayImageOptions displayImageOptions;


	private ConnectionRepository mConnectionRepository;
	private ConnectionFactoryRegistry mConnectionFactoryRegistry;

    public String getLanguage() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		return prefs.getString(PREFS_LANGUAGE, ALL);
	}

	public int getOrientation() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		String orientation = prefs.getString(PREFS_ORIENTATION, "landscape");

		if (orientation.equalsIgnoreCase("landscape")) {
			return ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE;
		} else {
			return ActivityInfo.SCREEN_ORIENTATION_PORTRAIT;
		}
	}

	public int getAppTheme() {
		String theme = getAppThemeString();

		if (theme.equalsIgnoreCase("dark"))
			return R.style.Theme_Sherlock;
		else if (theme.equalsIgnoreCase("light"))
			return R.style.Theme_Sherlock_Light;
		else
			return R.style.Theme_Sherlock_Light_DarkActionBar;
	}

	public boolean isDark() {
		int theme = getAppTheme();

        return theme == R.style.Theme_Sherlock;
	}

	public String getStreamImageSource() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		return prefs.getString(PREFS_STREAM_IMAGE, "screenshot");
	}

	public String getAppThemeString() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		return prefs.getString(PREFS_THEME, "lightDark");
	}

	public boolean getChat() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		return prefs.getBoolean(PREFS_CHAT, true);
	}

	public boolean isChatAutoScroll() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		return prefs.getBoolean(PREFS_CHAT_AUTOSCROLL, true);
	}

	public float getChatWidth() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		return prefs.getFloat(PREFS_CHAT_WIDTH, 0.5f);
	}

	public boolean isOfflineFavorites() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		return prefs.getBoolean(PREFS_OFFLINE_FAV, false);
	}

	public boolean getAdBlock() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		return prefs.getBoolean(PREFS_ADBLOCK_BOOLEAN, true);
	}

	void initialize() {
		// IMAGELOADER LIST ITEMS
		// Set up ImageLoader for list items
		L.disableLogging();
		ImageLoaderConfiguration imageLoaderConfigItems = new ImageLoaderConfiguration.Builder(
				getApplicationContext())
				.threadPoolSize(5)
				.threadPriority(Thread.MIN_PRIORITY + 2)
				.denyCacheImageMultipleSizesInMemory()
				.memoryCache(new UsingFreqLimitedMemoryCache(3 * 1024 * 1024))
				// 3 Mb
				.imageDownloader(
                        new BaseImageDownloader(this, 5 * 1000, 30 * 1000))
				// ConnectTimeout (5 s), readTimeout (30 s)
				.defaultDisplayImageOptions(DisplayImageOptions.createSimple())
				.build();
		imageLoader = ImageLoader.getInstance();
		imageLoader.init(imageLoaderConfigItems);
		// Set up ImageLoader for list items
		displayImageOptions = new DisplayImageOptions.Builder()
				//.showImageForEmptyUri(R.drawable.stub)
				//.showStubImage(R.drawable.stub)
				.resetViewBeforeLoading(true)
				.cacheInMemory(true)
				.imageScaleType(ImageScaleType.NONE)
				.bitmapConfig(Bitmap.Config.ARGB_8888).delayBeforeLoading(0)
				.displayer(new FadeInBitmapDisplayer(50)).build();

//		// INITIALIZE CONNECTION FACTORY
		mConnectionFactoryRegistry = new ConnectionFactoryRegistry();

		mConnectionFactoryRegistry.addConnectionFactory(
                new TwitchConnectionFactory(
                    new String(Base64Utils.decode(TwitchKey0+TwitchKey1)),
                    new String(Base64Utils.decode(TwitchSecret0+TwitchSecret1+TwitchSecret2+TwitchSecret3+TwitchSecret4+TwitchSecret5))));

		// JUSTIN
		mConnectionFactoryRegistry.addConnectionFactory(
                new JustinTVConnectionFactory(
                    new String(Base64Utils.decode(JustinTVKey0+JustinTVKey1)),
                    new String (Base64Utils.decode(JustinTVSecret0+JustinTVSecret1+JustinTVSecret2+JustinTVSecret3+JustinTVSecret4))));

		// SET UP DATABASE & ENCRYPTION
        SQLiteConnectionRepositoryHelper mRepositoryHelper = new SQLiteConnectionRepositoryHelper(this);

    	mConnectionRepository = new SQLiteConnectionRepository(
                mRepositoryHelper, mConnectionFactoryRegistry,
				AndroidEncryptors.text(new String(Base64Utils.decode(ConnRepoPass0 + ConnRepoPass1 + ConnRepoPass2 + ConnRepoPass3)),
                        new String(Base64Utils.decode(ConnRepoSalt0 + ConnRepoSalt1 + ConnRepoSalt2 + ConnRepoSalt3 + ConnRepoSalt4 + ConnRepoSalt5 + ConnRepoSalt6))));
	}

	public boolean isMature() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		return prefs.getBoolean(PREFS_MATURE_BOOLEAN, true);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		this.initialize();
	}

	public static String getLatestAPK() {
		// GET API VERSION
		if (Build.VERSION.SDK_INT >= 14) {
			return FLASH_APK_LATEST_4;
		} else {
			return FLASH_APK_LATEST_2_3;
		}
	}

	public boolean isAdobeFlashInstalled() {

		// CHECK FOR FLASH VERSION
		Intent adobe = new Intent();
		adobe.setComponent(new ComponentName("com.adobe.flashplayer",
				"com.adobe.flashplayer.FlashExpandableFileChooser"));
		PackageManager pm = getPackageManager();
        Assert.notNull(pm);
		List<ResolveInfo> activities = pm.queryIntentActivities(adobe, 0);

        return (activities != null && activities.size() > 0);
	}

	public float getChatAlpha() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		return prefs.getFloat(PREFS_CHAT_ALPHA, 1f);
	}

	public boolean showVideoDisclaimer() {
		SharedPreferences prefs = this.getSharedPreferences(APP_STRING,
				Context.MODE_PRIVATE);
		return prefs.getBoolean(SHOW_VIDEO_DISCLAIMER, true);
	}

	public void showVideoDisclaimerAgain(boolean value) {
		SharedPreferences prefs = this.getSharedPreferences(APP_STRING,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(SHOW_VIDEO_DISCLAIMER, value);
		editor.commit();
	}
	
	public boolean isNewVersion() {
		String version = getReleaseString();

		SharedPreferences prefs = this.getSharedPreferences(APP_STRING,
				Context.MODE_PRIVATE);
		return prefs.getBoolean(SHOW_RELEASE_NOTES + "_" + version, true);
	}

	public void showReleaseNotes(final Activity activity) {
		
		// SHOW NEW FEATURES ON NEW RELEASE
		String version = getReleaseString();
		String title = getResources().getString(R.string.release_notes_title);
		title = String.format(title, version);
		
		// SHOW ALERT DIALOG WITH MESSAGE
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
		
		// VIEW PREP
		RelativeLayout rl = (RelativeLayout) activity.getLayoutInflater().inflate(R.layout.whats_new_alert, null);
        Assert.notNull(rl);
		Button butt = (Button) rl.findViewById(R.id.twitterButton);
		butt.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_VIEW);
				intent.setData(Uri.parse(getResources().getString(R.string.twitter_url)));
				activity.startActivity(intent);
			}
		});
		
		builder
			.setIcon(R.drawable.ic_launcher)
			.setTitle(title)
			.setView(rl)
			.setPositiveButton(R.string.dialog_ok, new OnClickListener() {
				
				@Override
				public void onClick(DialogInterface dialog, int which) {
					showReleaseNotesAgain(false);
				}
			});
		
		AlertDialog ad = builder.create();
		ad.show();
		
	}

	String getReleaseString() {
		PackageInfo pInfo;
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
		} catch (NameNotFoundException e) {
			return "";
		}
		return pInfo.versionName;
	}

	void showReleaseNotesAgain(boolean value) {

		String version = getApplicationVersion();
		SharedPreferences prefs = this.getSharedPreferences(APP_STRING,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(SHOW_RELEASE_NOTES + "_"
				+ (version != null ? version : ""), value);
		editor.commit();

	}

	public String getApplicationVersion() {
		PackageInfo pInfo;
		String version;
		try {
			pInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
			version = pInfo.versionName;
		} catch (NameNotFoundException e) {
			return null;
		}
		return version;
	}

	public void shareStream(Context context, String subject, String content) {
		Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
		sharingIntent.setType("text/plain");
		sharingIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
		sharingIntent.putExtra(Intent.EXTRA_TEXT, content);
		context.startActivity(Intent.createChooser(sharingIntent, "Share via"));
	}

	public boolean showGestureArea() {
		SharedPreferences prefs = this.getSharedPreferences(APP_STRING,
				Context.MODE_PRIVATE);
		return prefs.getBoolean(SHOW_GESTURE, true);
	}

	public void setShowGestureArea(boolean value) {
		SharedPreferences prefs = this.getSharedPreferences(APP_STRING,
				Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(SHOW_GESTURE, value);
		editor.commit();
	}

	public Intent openInBrowserPath(String code, Context context) {
		String filename = "video.html";

		FileOutputStream fos;
		try {
			fos = openFileOutput(filename, Context.MODE_WORLD_READABLE);
			fos.write(code.getBytes());
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		File file = getFileStreamPath(filename);

        String url = "content://com.android.htmlfileprovider" + file.getPath();
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse(url), "text/html");


//		Intent intent = new Intent(Intent.ACTION_VIEW, Uri.fromFile(file));
		//intent.setDataAndType(Uri.fromFile(file), "application/xml");
		//intent.setData(Uri.fromFile(file));
		intent.addCategory(Intent.CATEGORY_BROWSABLE);
//		context.startActivity(Intent.createChooser(intent,
//				"Choose a browser that supports Flash"));
//        return intent;
		return	Intent.createChooser(intent,
				"Choose a browser that supports Flash");
	}

	public boolean isOpenInBrowser() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		return prefs.getBoolean("pref_alternative_flash", false);
	}

	public ConnectionRepository getConnectionRepository() {
		return mConnectionRepository;
	}

	public TwitchConnectionFactory getTwitchConnectionFactory() {
		return (TwitchConnectionFactory) mConnectionFactoryRegistry.getConnectionFactory(Twitch.class);
	}
	
	public JustinTVConnectionFactory getJustinTVConnectionFactory() {
		return (JustinTVConnectionFactory) mConnectionFactoryRegistry.getConnectionFactory(JustinTV.class);
	}
	
	public boolean hasStreamSourceEnabled() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		
		return (prefs.getBoolean(PREFS_JUSTIN_TV_ENABLED, false) ||
                prefs.getBoolean(PREFS_TWITCH_ENABLED, false));
	}

    public boolean isTwitchEnabled(){
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        return prefs.getBoolean(PREFS_TWITCH_ENABLED, false);
    }

    public boolean isJustinEnabled(){
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);

        return prefs.getBoolean(PREFS_JUSTIN_TV_ENABLED, false);
    }

	public boolean isFavorite(String mKey) {
		// TODO Auto-generated method stub
		return false;
	}

	public boolean isBrowserPlaybackEnabled() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		return prefs.getBoolean(PREFS_GENERAL_FLASH_BROWSER, false);
	}

	public boolean isHLS(Class<? extends StreamDetailFragment> class1) {
        SharedPreferences prefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        return prefs.getBoolean(PREFS_GENERAL_HLS, false) && class1 == TwitchStreamDetailFragment.class;
	}
}
