package co.bantamstudio.streamie.twitch.api;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import java.net.URI;

import co.bantamstudio.streamie.api.StreamieRequest;
import co.bantamstudio.streamie.auth.model.CategoriesWrapper;
import co.bantamstudio.streamie.auth.twitch.model.TwitchGamesWrapper;


public class TwitchCategoriesRequest extends StreamieRequest<CategoriesWrapper> {

	private final URI mUri;
	private static final String BASE_URL = TwitchApi.GAMES;

	public TwitchCategoriesRequest(int limit, int offset) {
		this(buildURI(BASE_URL, limit, offset));
	}
	
	public TwitchCategoriesRequest(String url) {
		this(buildURI(url));
	}
	
	private TwitchCategoriesRequest(URI uri) {
		super( CategoriesWrapper.class );
		mUri = uri;
	}

	@Override
	public TwitchGamesWrapper loadDataFromNetwork() throws Exception {
		ResponseEntity<TwitchGamesWrapper> responseEntity = getRestTemplate().exchange(mUri, HttpMethod.GET, new HttpEntity<Object>(TwitchApi.getHeaders()), TwitchGamesWrapper.class);
		return responseEntity.getBody();
	}

	@Override
	public Object getCacheKey() {
		return mUri.toString();
	}
}