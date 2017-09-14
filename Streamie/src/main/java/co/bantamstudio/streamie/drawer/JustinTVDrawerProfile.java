package co.bantamstudio.streamie.drawer;

import android.content.Context;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import co.bantamstudio.streamie.R;
import co.bantamstudio.streamie.adapter.DrawerAdapter;
import co.bantamstudio.streamie.auth.justintv.api.JustinTV;
import co.bantamstudio.streamie.auth.justintv.model.JustinTVProfile;
import co.bantamstudio.streamie.justintv.api.JustinTVProfileRequest;

class JustinTVDrawerProfile extends DrawerProfile {

	public JustinTVDrawerProfile(final Context context, SpiceManager spiceManager, JustinTV justinTV, DrawerAdapter adapter) {
		super(context, spiceManager, context.getResources().getString(R.string.justin_tv_profile_loading_top), context.getResources().getString(R.string.profile_loading_bottom));
		
		mDrawerAdapter = adapter;
		
		JustinTVProfileRequest request = new JustinTVProfileRequest(justinTV);
		mSpiceManager.execute(request, request.getCacheKey(), request.getCacheLength(),
				new RequestListener<JustinTVProfile>() {
					@Override
					public void onRequestFailure(
							SpiceException spiceException) {
                        String error = context.getResources().getString(R.string.profile_error_could_not_load);
                        Toast.makeText(mContext, error + ": " + spiceException.getLocalizedMessage(), Toast.LENGTH_LONG).show();
					}

					@Override
					public void onRequestSuccess(JustinTVProfile result) {
						mProfile = result;
						mDrawerAdapter.notifyDataSetChanged();
	                }
                });
    }
}
