package co.bantamstudio.streamie.activity;
import co.bantamstudio.streamie.R;

import co.bantamstudio.streamie.StreamieApplication;
import co.bantamstudio.streamie.ad.StreamieAd;
import co.bantamstudio.streamie.ad.AdLoader;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

// TODO: Auto-generated Javadoc
/**
 * The Class AdobeFlashActivity.
 */
public class AdobeFlashActivity extends SherlockActivity {

    /* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Set Theme
		/* The m application. */
        StreamieApplication mApplication = (StreamieApplication) getApplication();
		/* The m theme. */
        int mTheme = mApplication.getAppTheme();
		setTheme(mTheme);
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_adobe_flash);
		
		initialize();
	}
	
	/* (non-Javadoc)
	 * @see android.app.Activity#onStart()
	 */
	@Override
	protected void onStart() {
		// LOAD ADS
		StreamieAd ads = AdLoader.initialize(this);
		ads.loadBanner((ViewGroup) findViewById(R.id.ad_layout), null);		
		super.onStart();
	}
	
	/**
	 * Initialize.
	 */
	private void initialize(){
		// SET BUTTON LABEL FOR DOWNLOAD
		Button button = (Button) findViewById(R.id.ButtonDownloadDirect);
		String textFormat = getResources().getString(R.string.flash_download_apk);
		String text = String.format(textFormat, Build.VERSION.RELEASE);
		button.setText(text);
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockActivity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getSherlock().getMenuInflater().inflate(R.menu.regular, menu);
		getSupportActionBar().setDisplayOptions(
				ActionBar.DISPLAY_SHOW_CUSTOM | 
				ActionBar.DISPLAY_SHOW_HOME	| 
				ActionBar.DISPLAY_USE_LOGO |
				ActionBar.DISPLAY_SHOW_TITLE | 
				ActionBar.DISPLAY_HOME_AS_UP);
		
		return true;
	}

	/* (non-Javadoc)
	 * @see com.actionbarsherlock.app.SherlockActivity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			return true;
		case R.id.menu_settings:
			Intent intent = new Intent(getApplicationContext(),
			SettingsActivity.class);
			startActivity(intent);
			return true;
		case R.id.menu_feedback:
			StreamieApplication.sendFeedback(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	/**
	 * Watch video.
	 *
	 * @param v the v
	 */
	public void watchVideo(View v){
		Uri websiteUri = Uri.parse(StreamieApplication.FLASH_TUTORIAL_VIDEO);
		Intent intent = new Intent(Intent.ACTION_VIEW, websiteUri);
		startActivity(intent);
	}
	
	/**
	 * Download flash.
	 *
	 * @param v the v
	 */
	public void downloadFlash(View v){
		Uri websiteUri = Uri.parse(StreamieApplication.getLatestAPK());
		Intent intent = new Intent(Intent.ACTION_VIEW, websiteUri);
		startActivity(intent);
	}
	
	/**
	 * Visit archived flash page.
	 *
	 * @param v the v
	 */
	public void visitArchivedFlashPage(View v){
		Uri websiteUri = Uri.parse(StreamieApplication.FLASH_APK_ARCHIVE);
		Intent intent = new Intent(Intent.ACTION_VIEW, websiteUri);
		startActivity(intent);
	}
}
