package co.bantamstudio.streamie.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import org.springframework.util.StringUtils;

import co.bantamstudio.streamie.R;
import co.bantamstudio.streamie.StreamieApplication;
import co.bantamstudio.streamie.ad.AdLoader;
import co.bantamstudio.streamie.ad.StreamieAd;

public class GenericFragmentActivity extends SherlockFragmentActivity {

	private FragmentManager mFm;
	protected String mKey;
	private StreamieAd mAds;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// Set Theme
        StreamieApplication application = (StreamieApplication) getApplication();
		setTheme(application.getAppTheme());
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.frame_layout_ad);
		
		mFm = getSupportFragmentManager();

		initialize();
	}
	
	void initialize(){
		
		// LOAD ADS
		mAds = AdLoader.initialize(this);

		mAds.loadBanner((ViewGroup) findViewById(R.id.ad_layout), null);				
		super.onStart();
		
		Intent intent = getIntent();
		String fragmentClass = intent.getStringExtra(StreamieApplication.FRAGMENT);

        // SET TITLE
        String title = intent.getStringExtra(StreamieApplication.TITLE);
        if (StringUtils.hasLength(title))
            getSupportActionBar().setTitle(title);
		
		// LOAD FRAGMENT
		Bundle bdl = intent.getExtras();
		Fragment fragment = Fragment.instantiate(this, fragmentClass, bdl);
		mFm.beginTransaction().replace(R.id.container,  fragment).commit();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		this.getSherlock().getMenuInflater().inflate(R.menu.regular, menu);
		getSupportActionBar().setDisplayOptions(
				ActionBar.DISPLAY_SHOW_CUSTOM | 
				ActionBar.DISPLAY_SHOW_HOME	| 
				ActionBar.DISPLAY_USE_LOGO |
				ActionBar.DISPLAY_SHOW_TITLE |
				ActionBar.DISPLAY_HOME_AS_UP);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			break;
		case R.id.menu_settings:
			Intent intent = new Intent(getApplicationContext(),
			SettingsActivity.class);
			startActivity(intent);
			break;
		case R.id.menu_feedback:
			StreamieApplication.sendFeedback(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}
