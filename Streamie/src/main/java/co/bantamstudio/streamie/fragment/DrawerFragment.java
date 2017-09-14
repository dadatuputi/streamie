package co.bantamstudio.streamie.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.octo.android.robospice.JacksonSpringAndroidSpiceService;
import com.octo.android.robospice.SpiceManager;

import co.bantamstudio.streamie.HelperUtils;
import co.bantamstudio.streamie.R;
import co.bantamstudio.streamie.StreamieApplication;
import co.bantamstudio.streamie.activity.SettingsActivity;
import co.bantamstudio.streamie.adapter.DrawerAdapter;
import lombok.Setter;

public class DrawerFragment extends SherlockFragment {

	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */

	protected int mTheme;
	/**
	 * The {@link ViewPager} that will host the section contents.
	 */

	private ListView mDrawerList;
	private DrawerAdapter mAdapter;
	@Setter private DrawerLayout drawerParent;
	private StreamieApplication mApplication;

	private final SpiceManager mSpiceManager = new SpiceManager( JacksonSpringAndroidSpiceService.class );
    @Setter private FrameLayout drawer;
    private HelperUtils.FragmentManager mListener;

    @Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSpiceManager.start(getActivity());
	}
	
	@Override
	public void onDestroy() {
		mSpiceManager.shouldStop();
		super.onDestroy();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (mAdapter != null)
			mAdapter.reload();
	}

    @Override
    public void onAttach(Activity activity) {
        // MAKE SURE THAT THE ACTIVITY IS A FRAGMENTPUSHER
        super.onAttach(activity);
        try {
            mListener = (HelperUtils.FragmentManager) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement FragmentManager");
        }
    }

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		return inflater.inflate(R.layout.frame_layout_drawer,
				null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		this.mApplication = (StreamieApplication) getActivity()
				.getApplication();

		super.onCreate(savedInstanceState);

		initialize(savedInstanceState);
	}

	private void initialize(Bundle savedInstanceState) {

		// SET UP DRAWER

		mDrawerList = (ListView) getView().findViewById(R.id.listView1);

		// Capture button clicks on side menu
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		
		// CREATE ADAPTER AND ATTACH IT TO THE LIST
		mAdapter = new DrawerAdapter(getActivity(), mSpiceManager, mListener);
		mDrawerList.setAdapter(mAdapter);
	}

    // The click listener for ListView in the navigation drawer
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if (mAdapter != null && mAdapter.selectItem(position)) {
				mDrawerList.setItemChecked(position, true);
			}
			// Close drawer
			if (drawerParent != null & drawer != null)
				drawerParent.closeDrawer(drawer);
		}
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.regular, menu);
		inflater.inflate(R.menu.lookup, menu);

		// menu.
		//
		// getSupportActionBar().setDisplayOptions(
		// ActionBar.DISPLAY_SHOW_CUSTOM | ActionBar.DISPLAY_SHOW_HOME
		// | ActionBar.DISPLAY_USE_LOGO
		// | ActionBar.DISPLAY_SHOW_TITLE);
		//
		// return super.onCreateOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_settings:
			Intent intent = new Intent(getActivity().getApplicationContext(),
					SettingsActivity.class);
			startActivity(intent);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

    public void refreshDrawer(){
        if (mAdapter!=null) {
            mAdapter.reload();
        }
    }
}
