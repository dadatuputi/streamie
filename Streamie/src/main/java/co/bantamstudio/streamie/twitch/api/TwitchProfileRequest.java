package co.bantamstudio.streamie.twitch.api;

import com.octo.android.robospice.persistence.DurationInMillis;

import org.springframework.util.Assert;

import co.bantamstudio.streamie.api.StreamieRequest;
import co.bantamstudio.streamie.auth.twitch.api.Twitch;
import co.bantamstudio.streamie.auth.twitch.model.TwitchProfile;

public class TwitchProfileRequest extends StreamieRequest<TwitchProfile> {

	private final Twitch mTwitch;

	public TwitchProfileRequest(Twitch twitch) {
		super( TwitchProfile.class );
		Assert.notNull(twitch);
		mTwitch = twitch;
	}

	@Override
	public TwitchProfile loadDataFromNetwork() throws Exception {
		return mTwitch.userOperations().getUserProfile();
	}

	@Override
	public Object getCacheKey() {
		return "twitch_profile";
	}
	
	@Override
	public long getCacheLength() {
		return DurationInMillis.ONE_DAY;
	}
}
