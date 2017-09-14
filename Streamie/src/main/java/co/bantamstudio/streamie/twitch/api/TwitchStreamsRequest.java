package co.bantamstudio.streamie.twitch.api;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import co.bantamstudio.streamie.api.StreamieRequest;
import co.bantamstudio.streamie.auth.twitch.model.TwitchGame;
import co.bantamstudio.streamie.auth.twitch.model.TwitchStreamsWrapper;


public class TwitchStreamsRequest extends StreamieRequest<TwitchStreamsWrapper> {

	private final URI mUri;

	public TwitchStreamsRequest(String game, int limit, int offset) {
        this(buildURIQuery(TwitchApi.STREAMS, game, "", limit, offset));
	}
	
	public TwitchStreamsRequest(String url) {
		this(buildURI(url));
	}
	
	private TwitchStreamsRequest(URI uri) {
		super( TwitchStreamsWrapper.class );
		Assert.notNull(uri);
		mUri = uri;
	}

	@Override
	public TwitchStreamsWrapper loadDataFromNetwork() throws Exception {
		ResponseEntity<TwitchStreamsWrapper> responseEntity = getRestTemplate().exchange(mUri, HttpMethod.GET, new HttpEntity<Object>(TwitchApi.getHeaders()), TwitchStreamsWrapper.class);
		return responseEntity.getBody();
	}
	
	private static URI buildURIQuery(String baseUrl, String game, String channel, int limit, int offset) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl);
		
		if (StringUtils.hasLength(game) && !game.equalsIgnoreCase(TwitchGame.ALL_TWITCH_STREAMS))
			uriBuilder.queryParam("game", game);		
		if (StringUtils.hasLength(channel))
			uriBuilder.queryParam("channel", channel);	
		if (limit > 0)
			uriBuilder.queryParam("limit", Integer.toString(limit));	
		if (offset > 0)
			uriBuilder.queryParam("offset", Integer.toString(offset));	
		
		return uriBuilder.build().toUri();
	}

	@Override
	public Object getCacheKey() {
		return mUri.toString();
	}
}
