package co.bantamstudio.streamie.twitch.api;

import com.octo.android.robospice.persistence.DurationInMillis;

import org.springframework.util.Assert;

import co.bantamstudio.streamie.api.StreamieRequest;
import co.bantamstudio.streamie.auth.twitch.api.Twitch;

public class TwitchFollowsDeleteRequest extends StreamieRequest<String> {

	private final Twitch mTwitch;
	private final String mChannel;

	public TwitchFollowsDeleteRequest(Twitch twitch, String channel) {
		super( String.class );
		Assert.notNull(twitch);
		Assert.hasText(channel);
		mTwitch = twitch;
		mChannel = channel;
	}

	@Override
	public String loadDataFromNetwork() throws Exception {
		
		String usernameString = mTwitch.userOperations().getUserProfile().getName();
		
		return mTwitch.followsOperations().unFollowChannel(usernameString, mChannel);
		
	}

	@Override
	public Object getCacheKey() {
		// DON'T DO ANYTHING FOR DELETE REQUESTS
		return null;
	}
	
	@Override
	public long getCacheLength() {
		// DON'T CACHE DELETE REQUESTS
		return DurationInMillis.ALWAYS_EXPIRED;
	}
}