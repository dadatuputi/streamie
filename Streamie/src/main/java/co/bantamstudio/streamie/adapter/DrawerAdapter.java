package co.bantamstudio.streamie.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.octo.android.robospice.SpiceManager;

import co.bantamstudio.streamie.HelperUtils;
import co.bantamstudio.streamie.R;
import co.bantamstudio.streamie.StreamieApplication;
import co.bantamstudio.streamie.activity.SettingsActivity;
import co.bantamstudio.streamie.drawer.DrawerItem;
import co.bantamstudio.streamie.drawer.DrawerItem.DrawerItemCallback;
import co.bantamstudio.streamie.drawer.DrawerListItemImage;
import co.bantamstudio.streamie.drawer.JustinTVDrawer;
import co.bantamstudio.streamie.drawer.TwitchDrawer;
import co.bantamstudio.streamie.fragment.FeaturedCardFragment;
import lombok.Getter;

public class DrawerAdapter extends ArrayAdapter<DrawerItem> {
	private final StreamieApplication mApplication;
	private final Activity mActivity;
	private TwitchDrawer mTwitchDrawer;
	private JustinTVDrawer mJustinTVDrawer;
	private final SpiceManager mSpiceManager;
	private DrawerListItemImage mSettingsDrawer;
    private DrawerListItemImage mFeaturedStreams;
	private DrawerListItemImage mAppInfoDrawer;
	private final LayoutInflater mInflater;
    @Getter private final HelperUtils.FragmentManager listener;

	public enum RowType {
		LIST_ITEM, HEADER_ITEM
	}
	
	public DrawerAdapter(Activity activity,
			SpiceManager spiceManager, HelperUtils.FragmentManager listener) {
		super(activity, 0);
        mInflater = LayoutInflater.from(activity);
		mActivity = activity;
        this.listener = listener;
		mApplication = (StreamieApplication) activity.getApplication();
		mSpiceManager = spiceManager;
	}

	private void initialize() {

        // FEATURED FRAGMENT ENTRY
        if (mFeaturedStreams == null) {
            mFeaturedStreams = new DrawerListItemImage(mActivity.getResources().getString(R.string.featured_streams_title),
                    "",
                    mActivity.getResources().getDrawable(R.drawable.ic_action_featured));
            mFeaturedStreams.setOnClickCallback(new DrawerItemCallback() {
                @Override
                public void onClickCallback() {
                    // LOAD FEATURED STREAMS FRAGMENT
                    Bundle bdl = new Bundle();
                    bdl.putString(StreamieApplication.TITLE, mActivity.getResources().getString(R.string.featured_streams_title));
                    getListener().pushFragment(FeaturedCardFragment.class, bdl);
                }
            });
        }
        add(mFeaturedStreams);

        // TWITCH GROUP
        if (mApplication.isTwitchEnabled())
            mTwitchDrawer = new TwitchDrawer(mActivity, mSpiceManager, this);

        // JUSTIN GROUP
        if (mApplication.isJustinEnabled())
            mJustinTVDrawer = new JustinTVDrawer(mActivity, mSpiceManager, this);
		
		// SETTINGS ENTRY
		if (mSettingsDrawer == null) {
			mSettingsDrawer = new DrawerListItemImage("Settings", "", mActivity.getResources().getDrawable(R.drawable.ic_sysbar_quicksettings));
			mSettingsDrawer.setOnClickCallback(new DrawerItemCallback() {
	
				@Override
				public void onClickCallback() {
					// LOAD SETTINGS ACTIVITY
					mActivity.startActivity(new Intent(mActivity
							.getApplicationContext(), SettingsActivity.class));
				}
			});
		}
		add(mSettingsDrawer);

		// APP INFO
		if (mAppInfoDrawer == null) {
			mAppInfoDrawer = new DrawerListItemImage(mActivity
					.getResources().getString(R.string.app_name),
					mApplication.getApplicationVersion(), mActivity.getResources()
							.getDrawable(R.drawable.ic_action_streamie));
			
			mAppInfoDrawer.setOnClickCallback(new DrawerItemCallback() {
				
				@Override
				public void onClickCallback() {
					mApplication.showReleaseNotes(mActivity);
				}
			});
		}
		
		add(mAppInfoDrawer);
		
		notifyDataSetChanged();
	}
	
	public void reload() {
		clear();
		initialize();
	}

	public boolean selectItem(int position) {
		return getItem(position).onClick();
	}
	
    @Override
    public int getViewTypeCount() {
        return RowType.values().length;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getViewType();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getItem(position).getView(mInflater, convertView);
    }
}