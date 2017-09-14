package co.bantamstudio.streamie.twitch.api;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;

import java.net.URI;

import co.bantamstudio.streamie.api.StreamieRequest;
import co.bantamstudio.streamie.auth.twitch.model.TwitchStreamsWrapper;

public class TwitchStreamSearchRequest extends StreamieRequest<TwitchStreamsWrapper> {

	private final URI mUri;

	public TwitchStreamSearchRequest(String query, int limit, int offset) {
		this(buildSearchURIQuery(TwitchApi.SEARCH_STREAMS, query, limit, offset));
	}
	
	public TwitchStreamSearchRequest(String url) {
		this(buildURI(url));
	}
	
	private TwitchStreamSearchRequest(URI uri) {
		super( TwitchStreamsWrapper.class );
		Assert.notNull(uri);
		mUri = uri;
	}

	@Override
	public TwitchStreamsWrapper loadDataFromNetwork() throws Exception {
		ResponseEntity<TwitchStreamsWrapper> responseEntity = getRestTemplate().exchange(mUri, HttpMethod.GET, new HttpEntity<Object>(TwitchApi.getHeaders()), TwitchStreamsWrapper.class);
		return responseEntity.getBody();
	}

	@Override
	public Object getCacheKey() {
		return mUri.toString();
	}
}
