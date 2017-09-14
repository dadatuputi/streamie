package co.bantamstudio.streamie.twitch.api;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

import java.net.URI;

import co.bantamstudio.streamie.api.StreamieRequest;
import co.bantamstudio.streamie.auth.twitch.model.TwitchFeaturedStreamsWrapper;


public class TwitchFeaturedStreamsRequest extends StreamieRequest<TwitchFeaturedStreamsWrapper> {

	private final URI mUri;

	public TwitchFeaturedStreamsRequest(int limit, int offset) {
		this(buildURI(TwitchApi.STREAMS_FEATURED, limit, offset));
	}
	
	public TwitchFeaturedStreamsRequest(String url) {
		this(buildURI(url));
	}
	
	private TwitchFeaturedStreamsRequest(URI uri) {
		super( TwitchFeaturedStreamsWrapper.class );
		Assert.notNull(uri);
		mUri = uri;
	}

	@Override
	public TwitchFeaturedStreamsWrapper loadDataFromNetwork() throws Exception {
		ResponseEntity<TwitchFeaturedStreamsWrapper> responseEntity = getRestTemplate().exchange(mUri, HttpMethod.GET, new HttpEntity<Object>(TwitchApi.getHeaders()), TwitchFeaturedStreamsWrapper.class);
		return responseEntity.getBody();
	}

	@Override
	public Object getCacheKey() {
		return mUri.toString();
	}
}
