package co.bantamstudio.streamie.drawer;

import android.content.Context;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import co.bantamstudio.streamie.R;
import co.bantamstudio.streamie.adapter.DrawerAdapter;
import co.bantamstudio.streamie.auth.twitch.api.Twitch;
import co.bantamstudio.streamie.auth.twitch.model.TwitchProfile;
import co.bantamstudio.streamie.twitch.api.TwitchProfileRequest;

class TwitchDrawerProfile extends DrawerProfile {

	public TwitchDrawerProfile(final Context context, SpiceManager spiceManager, Twitch twitch, DrawerAdapter adapter) {
		super(context, spiceManager, context.getResources().getString(R.string.twitch_profile_loading_top), context.getResources().getString(R.string.profile_loading_bottom));
		
		mDrawerAdapter = adapter;
		
		TwitchProfileRequest request = new TwitchProfileRequest(twitch);
		mSpiceManager.execute(request, request.getCacheKey(), request.getCacheLength(),
				new RequestListener<TwitchProfile>() {
					@Override
					public void onRequestFailure(
							SpiceException spiceException) {
                        String error = context.getResources().getString(R.string.profile_error_could_not_load);
						Toast.makeText(mContext, error + ": " + spiceException.getLocalizedMessage(), Toast.LENGTH_LONG).show();
					}

					@Override
					public void onRequestSuccess(TwitchProfile result) {
						mProfile = result;
						mDrawerAdapter.notifyDataSetChanged();
					}
				});
	}
}
