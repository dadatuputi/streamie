package co.bantamstudio.streamie.twitch.api;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import co.bantamstudio.streamie.api.StreamieRequest;
import co.bantamstudio.streamie.auth.model.CategoriesWrapper;
import co.bantamstudio.streamie.auth.twitch.model.TwitchGamesSearchWrapper;


public class TwitchCategorySearchRequest extends StreamieRequest<CategoriesWrapper> {

	private final URI mUri;

	public TwitchCategorySearchRequest(String query, int limit, int offset) {
		this(buildGameSearchURIQuery(TwitchApi.SEARCH_GAMES, query, limit, offset));
	}
	
	public TwitchCategorySearchRequest(String url) {
		this(buildURI(url));
	}
	
	private TwitchCategorySearchRequest(URI uri) {
		super( CategoriesWrapper.class );
		Assert.notNull(uri);
		mUri = uri;
	}

	@Override
	public TwitchGamesSearchWrapper loadDataFromNetwork() throws Exception {
		ResponseEntity<TwitchGamesSearchWrapper> responseEntity = getRestTemplate().exchange(mUri, HttpMethod.GET, new HttpEntity<Object>(TwitchApi.getHeaders()), TwitchGamesSearchWrapper.class);
		return responseEntity.getBody();
	}
	
	private static URI buildGameSearchURIQuery(String baseUrl, String query, int limit, int offset) {
		Assert.hasText(baseUrl);
		Assert.hasText(query);
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl);
		
		uriBuilder.queryParam("q", query);
		uriBuilder.queryParam("type", "suggest");
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
