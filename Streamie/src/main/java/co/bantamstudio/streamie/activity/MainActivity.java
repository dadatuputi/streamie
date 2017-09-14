package co.bantamstudio.streamie.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.Window;
import android.widget.FrameLayout;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.octo.android.robospice.JacksonSpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;

import co.bantamstudio.streamie.HelperUtils;
import co.bantamstudio.streamie.R;
import co.bantamstudio.streamie.StreamieApplication;
import co.bantamstudio.streamie.activity.SettingsActivity.ProviderPreferenceActivity;
import co.bantamstudio.streamie.fragment.DrawerFragment;
import co.bantamstudio.streamie.fragment.FeaturedCardFragment;
import co.bantamstudio.streamie.justintv.JustinTVStreamDetailFragment;
import co.bantamstudio.streamie.twitch.TwitchStreamDetailFragment;

public class MainActivity extends SherlockFragmentActivity implements
        HelperUtils.FragmentManager,
        HelperUtils.DrawerHandler{

	private int mTheme;
	private DrawerLayout mDrawerLayout;
	private StreamieApplication mApplication;
	private ActionBarDrawerToggle mDrawerToggle;
	private FrameLayout mDrawerView;

	private final SpiceManager spiceManager = new SpiceManager( JacksonSpringAndroidSpiceService.class );
    private DrawerFragment mDrawerFragment;

    @Override
	protected void onStart() {
		super.onStart();
		spiceManager.start(this);
	}

	@Override
	protected void onStop() {
		spiceManager.shouldStop();
		super.onStop();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// Set Theme
		this.mApplication = (StreamieApplication) getApplication();
		mTheme = mApplication.getAppTheme();
		setTheme(mTheme);
		
		getWindow().requestFeature(Window.FEATURE_PROGRESS);

		super.onCreate(savedInstanceState);
		setContentView(R.layout.frame_layout_drawer_ad);

        // CHECK FOR URL INTENTS
        Uri data = getIntent().getData();
        if (data != null) {
            String dataString = data.getLastPathSegment();
            if (dataString != null && !dataString.equalsIgnoreCase("")) {
                // LOAD THE STREAM
                Intent intent = new Intent(getApplicationContext(),
                        GenericFragmentActivity.class);
                intent.putExtra(StreamieApplication.KEY, dataString);
                // LOAD JUSTIN.TV INTENT
                if (data.toString().contains("twitch.tv")) {
                    intent.putExtra(StreamieApplication.FRAGMENT,
                            TwitchStreamDetailFragment.class.getName());
                    startActivity(intent);

                } else if (data.toString().contains("justin.tv")) {
                    intent.putExtra(StreamieApplication.FRAGMENT,
                            JustinTVStreamDetailFragment.class.getName());
                    startActivity(intent);
                }
            }
        }

        if (!mApplication.hasStreamSourceEnabled()) {
            // START PREFERENCE ACTIVITY IF NO PROVIDERS ARE SELECTED
            Intent intent = new Intent(getApplicationContext(),	ProviderPreferenceActivity.class);
            startActivity(intent);
            finish();
        } else {
            initialize(savedInstanceState);
        }
    }

    private void initialize(Bundle savedInstanceState) {

        // START UP FRONT PAGE FRAGMENT
        if (savedInstanceState == null) {
            Bundle bdl = new Bundle();
            bdl.putString(StreamieApplication.TITLE, getResources().getString(R.string.featured_streams_title));
            pushFragment(FeaturedCardFragment.class, bdl);
        }

        // SET UP DRAWER
        mDrawerView = (FrameLayout) findViewById(R.id.left_drawer);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
                GravityCompat.START);

        // ActionBarDrawerToggle ties together the the proper interactions
        // between the sliding drawer and the action bar app icon
        mDrawerToggle = new ActionBarDrawerToggle(this,
                mDrawerLayout,
                R.drawable.ic_drawer,
                R.string.drawer_open,
                R.string.drawer_close);

        mDrawerLayout.setDrawerListener(mDrawerToggle);

        // CREATE DRAWER FRAGMENT
        if (savedInstanceState == null) {
            mDrawerFragment = (DrawerFragment) Fragment.instantiate(this,
                    DrawerFragment.class.getName());
            mDrawerFragment.setDrawerParent(mDrawerLayout);
            mDrawerFragment.setDrawer(mDrawerView);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.left_drawer, mDrawerFragment, DrawerFragment.class.getName()).commit();
        } else {
            mDrawerFragment = (DrawerFragment) getSupportFragmentManager().findFragmentByTag(DrawerFragment.class.getName());
        }

        // SHOW RELEASE NOTES
        if (mApplication.isNewVersion())
            mApplication.showReleaseNotes(this);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportActionBar().setDisplayOptions(
				ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME
						| ActionBar.DISPLAY_USE_LOGO
						| ActionBar.DISPLAY_SHOW_TITLE
						| ActionBar.DISPLAY_HOME_AS_UP);

		return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			if (mDrawerLayout.isDrawerOpen(mDrawerView)) {
				closeDrawer();
			} else {
                openDrawer();
			}
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void pushFragment(Class<? extends Fragment> fragmentClass, Bundle bundle) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(fragmentClass.getName());
        if (fragment == null) {
            // CREATE FRAGMENT
            fragment = Fragment.instantiate(this, fragmentClass.getName());
        }

        fragment.setRetainInstance(true);

        // ATTACH ARGS
        if (bundle != null && fragment.getArguments() == null)
            fragment.setArguments(bundle);

        // PUSH FRAGMENT ONTO SCREEN
        if (!fragment.isVisible()) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.container, fragment, fragmentClass.getName());
            if (getSupportFragmentManager().getFragments() != null)
                ft.addToBackStack(fragmentClass.getName());
            ft.commit();
        }

        closeDrawer();
    }

    @Override
    public void popFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        getSupportFragmentManager().popBackStackImmediate();
    }

    @Override
    public void openDrawer() {
        if (mDrawerLayout != null && mDrawerView != null)
            mDrawerLayout.openDrawer(mDrawerView);
    }

    @Override
    public void closeDrawer() {
        if (mDrawerLayout != null && mDrawerView != null)
            mDrawerLayout.closeDrawer(mDrawerView);
    }

    @Override
    public void refreshDrawer() {
        mDrawerFragment.refreshDrawer();
    }
}


