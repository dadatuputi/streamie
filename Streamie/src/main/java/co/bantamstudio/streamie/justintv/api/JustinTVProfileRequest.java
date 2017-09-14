package co.bantamstudio.streamie.justintv.api;

import com.octo.android.robospice.persistence.DurationInMillis;

import org.springframework.util.Assert;

import co.bantamstudio.streamie.api.StreamieRequest;
import co.bantamstudio.streamie.auth.justintv.api.JustinTV;
import co.bantamstudio.streamie.auth.justintv.model.JustinTVProfile;

public class JustinTVProfileRequest extends StreamieRequest<JustinTVProfile> {

	private final JustinTV mJustinTV;

	public JustinTVProfileRequest(JustinTV justinTV) {
		super( JustinTVProfile.class );
		Assert.notNull(justinTV);
		mJustinTV = justinTV;
	}

	@Override
	public JustinTVProfile loadDataFromNetwork() throws Exception {
		return mJustinTV.userOperations().getUserProfile();
	}

	@Override
	public Object getCacheKey() {
		return "justin_tv_profile";
	}
	
	@Override
	public long getCacheLength() {
		return DurationInMillis.ONE_DAY;
	}
}
