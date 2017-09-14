package co.bantamstudio.streamie.drawer;

import android.app.Activity;
import android.os.Bundle;

import com.octo.android.robospice.SpiceManager;

import org.springframework.social.connect.ConnectionRepository;

import co.bantamstudio.streamie.R;
import co.bantamstudio.streamie.StreamieApplication;
import co.bantamstudio.streamie.adapter.DrawerAdapter;
import co.bantamstudio.streamie.auth.justintv.api.JustinTV;
import co.bantamstudio.streamie.auth.justintv.model.JustinTVProfile;
import co.bantamstudio.streamie.drawer.DrawerItem.DrawerItemCallback;
import co.bantamstudio.streamie.justintv.JustinTVAuthFragment;
import co.bantamstudio.streamie.justintv.JustinTVCategoryListFragment;
import co.bantamstudio.streamie.justintv.JustinTVFavoriteListFragment;
import co.bantamstudio.streamie.justintv.JustinTVSearchListFragment;

public class JustinTVDrawer {

	private final Activity mActivity;
	private final ConnectionRepository mConnectionRepository;
	JustinTVProfile mUserProfile;
	private final SpiceManager mSpiceManager;
	private final StreamieApplication mApplication;
	private final DrawerAdapter mDrawerAdapter;
	private DrawerHeader mDrawerHeader;
	private DrawerListItemImage mConnectItem;
	private DrawerListItemImage mSearchItem;
	private JustinTVDrawerProfile mProfileItem;
	private DrawerListItemImage mFavorites;

	public JustinTVDrawer(Activity activity,
                          SpiceManager spiceManager, DrawerAdapter adapter) {
		mActivity = activity;
		mDrawerAdapter = adapter;
		mApplication = (StreamieApplication) activity.getApplication();
		mConnectionRepository = mApplication.getConnectionRepository();
		mSpiceManager = spiceManager;
        initialize();
	}

	private void initialize() {
		// JUSTIN HEADER WITH LOGO
		if (mDrawerHeader == null) {
			mDrawerHeader = new DrawerHeader("", mActivity.getResources()
				.getDrawable(R.drawable.jtv_logo));
            mDrawerHeader.setOnClickCallback(new DrawerItemCallback() {
                @Override
                public void onClickCallback() {
                    // LOAD JUSTIN FRAGMENT
                    Bundle bdl = new Bundle();
                    bdl.putString(StreamieApplication.TITLE, mActivity.getResources().getString(R.string.justin_tv));
                    bdl.putString(StreamieApplication.SUBTITLE, mActivity.getResources().getString(R.string.categories));
                    mDrawerAdapter.getListener().pushFragment(JustinTVCategoryListFragment.class,
                        bdl);
                }
            });
		}
		mDrawerAdapter.add(mDrawerHeader);

//		if (isConnected(mConnectionRepository))
//			buildAuth();
//		else
//			buildUnAuth();

		// SEARCH ITEM
		if (mSearchItem == null) {
			mSearchItem = new DrawerListItemImage(mActivity.getResources().getString(R.string.justin_tv_search_channels_hint), "",
				mActivity.getResources().getDrawable(R.drawable.abs__ic_search));
				mSearchItem.setOnClickCallback(new DrawerItemCallback() {
	
				@Override
				public void onClickCallback() {
					// LOAD SEARCH FRAGMENT
                    Bundle bdl = new Bundle();
                    bdl.putString(StreamieApplication.TITLE, mActivity.getResources().getString(R.string.justin_tv));
                    bdl.putString(StreamieApplication.SUBTITLE, mActivity.getResources().getString(R.string.channel_search));
                    mDrawerAdapter.getListener().pushFragment(JustinTVSearchListFragment.class,
                        bdl);
				}
			});
		}
		mDrawerAdapter.add(mSearchItem);

        mDrawerAdapter.add(new DrawerSeparator());
	}

	private static boolean isConnected(ConnectionRepository connectionRepository) {
		return connectionRepository.findPrimaryConnection(JustinTV.class) != null;
	}
	
	private void buildAuth() {
		// DISPLAY JUSTIN PROFILE
		if (mProfileItem == null) {
			JustinTV justinTV = mConnectionRepository.findPrimaryConnection(JustinTV.class)
					.getApi();
			mProfileItem = new JustinTVDrawerProfile(mActivity, mSpiceManager, justinTV, mDrawerAdapter);
		}
		
		mDrawerAdapter.add(mProfileItem);
			
		// SHOW FAVORITES LIST
		if (mFavorites == null) {
			mFavorites = new DrawerListItemImage(mActivity.getResources().getString(R.string.followed_channels), "", mActivity.getResources().getDrawable(R.drawable.ic_menu_star));
			mFavorites.setOnClickCallback(new DrawerItemCallback() {
				
				@Override
				public void onClickCallback() {
					// SHOW FOLLOWED CHANNELS
                    Bundle bdl = new Bundle();
                    bdl.putString(StreamieApplication.TITLE, mActivity.getResources().getString(R.string.justin_tv));
                    bdl.putString(StreamieApplication.SUBTITLE, mActivity.getResources().getString(R.string.followed_channels));
                    mDrawerAdapter.getListener().pushFragment(JustinTVFavoriteListFragment.class,
                            bdl);
				}
			});
		}
		
		mDrawerAdapter.add(mFavorites);
	}
	
	private void buildUnAuth() {
		// IF NOT CONNECTED TO JUSTIN.TV, ASK TO CONNECT
		if (mConnectItem == null) {
			mConnectItem = new DrawerListItemImage(mActivity.getResources().getString(R.string.pref_justin_tv_log_in_title), "", mActivity.getResources().getDrawable(R.drawable.ic_action_connect));
			mConnectItem.setOnClickCallback(new DrawerItemCallback() {

				@Override
				public void onClickCallback() {
                    Bundle bdl = new Bundle();
                    bdl.putString(StreamieApplication.TITLE, mActivity.getResources().getString(R.string.justin_tv));
                    bdl.putString(StreamieApplication.SUBTITLE, mActivity.getResources().getString(R.string.authenticate));
                    mDrawerAdapter.getListener().pushFragment(JustinTVAuthFragment.class,
                            bdl);
				}
			});
		}
		mDrawerAdapter.add(mConnectItem);
	}
}
