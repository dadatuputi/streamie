package co.bantamstudio.streamie.drawer;

import android.app.Activity;
import android.os.Bundle;

import com.octo.android.robospice.SpiceManager;

import org.springframework.social.connect.ConnectionRepository;

import co.bantamstudio.streamie.R;
import co.bantamstudio.streamie.StreamieApplication;
import co.bantamstudio.streamie.adapter.DrawerAdapter;
import co.bantamstudio.streamie.auth.twitch.api.Twitch;
import co.bantamstudio.streamie.drawer.DrawerItem.DrawerItemCallback;
import co.bantamstudio.streamie.twitch.TwitchAuthFragment;
import co.bantamstudio.streamie.twitch.TwitchCategoryListFragment;
import co.bantamstudio.streamie.twitch.TwitchCategorySearchListFragment;
import co.bantamstudio.streamie.twitch.TwitchFavoriteListFragment;
import co.bantamstudio.streamie.twitch.TwitchStreamSearchListFragment;

public class TwitchDrawer {

	private final Activity mActivity;
	private final ConnectionRepository mConnectionRepository;
	private final SpiceManager mSpiceManager;
	private final DrawerAdapter mDrawerAdapter;
	private DrawerHeader mHeaderItem;
	private TwitchDrawerProfile mProfileItem;
	private DrawerListItemImage mFavorites;
	private DrawerListItemImage mConnectItem;
	private DrawerListItemImage mSearchTwitchChannels;
	private DrawerListItemImage mSearchTwitchGames;

	public TwitchDrawer(Activity activity,
			SpiceManager spiceManager, DrawerAdapter adapter) {
		mActivity = activity;
		mDrawerAdapter = adapter;
        StreamieApplication application = (StreamieApplication) activity.getApplication();
		mConnectionRepository = application.getConnectionRepository();
		mSpiceManager = spiceManager;
        initialize();
	}

	private void initialize() {

		// TWITCH HEADER ITEM
		if (mHeaderItem == null) {
			mHeaderItem = new DrawerHeader("", mActivity.getResources()
				.getDrawable(R.drawable.twitch_logo));
            mHeaderItem.setOnClickCallback(new DrawerItemCallback() {
                @Override
                public void onClickCallback() {
                    // LOAD TWITCH FRAGMENT
                    Bundle bdl = new Bundle();
                    bdl.putString(StreamieApplication.TITLE, mActivity.getResources().getString(R.string.twitch));
                    bdl.putString(StreamieApplication.SUBTITLE, mActivity.getResources().getString(R.string.games));
                    mDrawerAdapter.getListener().pushFragment(TwitchCategoryListFragment.class,
                            bdl);
                }
            });
		}
		
		mDrawerAdapter.add(mHeaderItem);
		
		// DISPLAY TWITCH PROFILE
		if (isConnected(mConnectionRepository)) {
			buildAuth();			
		} else {
			buildDeAuth();
		}
		
		// SEARCH CHANNELS
		if (mSearchTwitchChannels == null) {
			mSearchTwitchChannels = new DrawerListItemImage(mActivity.getResources().getString(R.string.twitch_search_channels), "", mActivity.getResources().getDrawable(R.drawable.abs__ic_search));
			mSearchTwitchChannels.setOnClickCallback(new DrawerItemCallback() {
				
				@Override
				public void onClickCallback() {
					// LOAD SEARCH FRAGMENT
                    Bundle bdl = new Bundle();
                    bdl.putString(StreamieApplication.TITLE, mActivity.getResources().getString(R.string.twitch));
                    bdl.putString(StreamieApplication.SUBTITLE, mActivity.getResources().getString(R.string.channel_search));
                    mDrawerAdapter.getListener().pushFragment(TwitchStreamSearchListFragment.class,
                            bdl);
				}
			});
		}
		mDrawerAdapter.add(mSearchTwitchChannels);
		
		// SEARCH GAMES
		if (mSearchTwitchGames == null) {
			mSearchTwitchGames = new DrawerListItemImage(mActivity.getResources().getString(R.string.twitch_search_games), "", mActivity.getResources().getDrawable(R.drawable.abs__ic_search));
			mSearchTwitchGames.setOnClickCallback(new DrawerItemCallback() {
				
				@Override
				public void onClickCallback() {
					// LOAD SEARCH FRAGMENT
                    Bundle bdl = new Bundle();
                    bdl.putString(StreamieApplication.TITLE, mActivity.getResources().getString(R.string.twitch));
                    bdl.putString(StreamieApplication.SUBTITLE, mActivity.getResources().getString(R.string.twitch_search_category_title));
                    mDrawerAdapter.getListener().pushFragment(TwitchCategorySearchListFragment.class,
                            bdl);
				}
			});
		}
		mDrawerAdapter.add(mSearchTwitchGames);

        mDrawerAdapter.add(new DrawerSeparator());
	}
	
	private void buildAuth() {
		// SHOW PROFILE
		if (mProfileItem == null) {
			Twitch twitch = mConnectionRepository.findPrimaryConnection(Twitch.class)
					.getApi();
			mProfileItem = new TwitchDrawerProfile(mActivity, mSpiceManager, twitch, mDrawerAdapter);
		}
		
		mDrawerAdapter.add(mProfileItem);
			
		// SHOW FAVORITES LIST
		if (mFavorites == null) {
			mFavorites = new DrawerListItemImage(mActivity.getResources().getString(R.string.followed_channels), "", mActivity.getResources().getDrawable(R.drawable.ic_menu_star));
			mFavorites.setOnClickCallback(new DrawerItemCallback() {
				
				@Override
				public void onClickCallback() {
					// PUSH FAVORITES FRAGMENT
                    Bundle bdl = new Bundle();
                    bdl.putString(StreamieApplication.TITLE, mActivity.getResources().getString(R.string.twitch));
                    bdl.putString(StreamieApplication.SUBTITLE, mActivity.getResources().getString(R.string.followed_channels));
                    mDrawerAdapter.getListener().pushFragment(TwitchFavoriteListFragment.class,
                            bdl);
				}
			});
		}
		
		mDrawerAdapter.add(mFavorites);
	}
	
	private void buildDeAuth() {
		// IF NOT CONNECTED TO TWITCH, ASK TO CONNECT
		if (mConnectItem == null) {
			mConnectItem = new DrawerListItemImage(mActivity.getResources().getString(R.string.pref_twitch_log_in_title), "", mActivity.getResources().getDrawable(R.drawable.ic_action_connect));
			mConnectItem.setOnClickCallback(new DrawerItemCallback() {
	
				@Override
				public void onClickCallback() {
                    // PUSH CONNECT FRAGMENT
                    Bundle bdl = new Bundle();
                    bdl.putString(StreamieApplication.TITLE, mActivity.getResources().getString(R.string.twitch));
                    bdl.putString(StreamieApplication.SUBTITLE, mActivity.getResources().getString(R.string.authenticate));
                    mDrawerAdapter.getListener().pushFragment(TwitchAuthFragment.class,
                            bdl);
				}});
		}

		mDrawerAdapter.add(mConnectItem);
	}

	private static boolean isConnected(ConnectionRepository connectionRepository) {
		return connectionRepository.findPrimaryConnection(Twitch.class) != null;
	}
}
