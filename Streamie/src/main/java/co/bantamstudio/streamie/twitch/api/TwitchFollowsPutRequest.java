package co.bantamstudio.streamie.twitch.api;

import com.octo.android.robospice.persistence.DurationInMillis;

import org.springframework.util.Assert;

import co.bantamstudio.streamie.api.StreamieRequest;
import co.bantamstudio.streamie.auth.twitch.api.Twitch;
import co.bantamstudio.streamie.auth.twitch.model.TwitchChannelWrapper;

public class TwitchFollowsPutRequest extends StreamieRequest<TwitchChannelWrapper> {

	private final Twitch mTwitch;
	private final String mChannel;

	public TwitchFollowsPutRequest(Twitch twitch, String channel) {
		super( TwitchChannelWrapper.class );
		Assert.notNull(twitch);
		Assert.hasText(channel);
		mTwitch = twitch;
		mChannel = channel;
	}

	@Override
	public TwitchChannelWrapper loadDataFromNetwork() throws Exception {
		
		String usernameString = mTwitch.userOperations().getUserProfile().getName();
		
		return mTwitch.followsOperations().followChannel(usernameString, mChannel);
		
	}

	@Override
	public Object getCacheKey() {
		// DO NOTHING FOR PUT REQUESTS
		return null;
	}
	
	@Override
	public long getCacheLength() {
		// DON'T CACHE PUT REQUESTS
		return DurationInMillis.ALWAYS_EXPIRED;
	}
}