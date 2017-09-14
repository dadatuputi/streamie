package co.bantamstudio.streamie.twitch.api;

import org.springframework.util.Assert;

import java.net.URI;

import co.bantamstudio.streamie.api.StreamieRequest;
import co.bantamstudio.streamie.auth.twitch.api.Twitch;
import co.bantamstudio.streamie.auth.twitch.model.TwitchStreamsWrapper;

public class TwitchFollowsGetRequest extends StreamieRequest<TwitchStreamsWrapper> {

	private final Twitch mTwitch;
	private final URI mUri;
	private static final String BASE_URL = TwitchApi.FOLLOWS;

	public TwitchFollowsGetRequest(Twitch twitch, int limit, int offset) {
		this(twitch, buildURI(BASE_URL, limit, offset));
	}
	
	public TwitchFollowsGetRequest(Twitch twitch, String url) {
		this(twitch, buildURI(url));
	}
	
	private TwitchFollowsGetRequest(Twitch twitch, URI uri) {
		super( TwitchStreamsWrapper.class );
		Assert.notNull(uri);
		Assert.notNull(twitch);
		mUri = uri;
		mTwitch = twitch;
	}

	@Override
	public TwitchStreamsWrapper loadDataFromNetwork() throws Exception {
		return mTwitch.followsOperations().getFollows(mUri);
	}

	@Override
	public Object getCacheKey() {
		return mUri.toString();
	}
}