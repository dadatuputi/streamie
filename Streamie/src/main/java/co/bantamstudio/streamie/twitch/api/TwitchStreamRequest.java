package co.bantamstudio.streamie.twitch.api;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import co.bantamstudio.streamie.api.StreamieRequest;
import co.bantamstudio.streamie.auth.twitch.model.TwitchStreamWrapper;


public class TwitchStreamRequest extends StreamieRequest<TwitchStreamWrapper> {

    private final URI mUri;

    public TwitchStreamRequest(String user) {
		super( TwitchStreamWrapper.class );
		Assert.notNull(user);
		mUri = buildURIQuery(TwitchApi.streamList, user);
	}

	@Override
	public TwitchStreamWrapper loadDataFromNetwork() throws Exception {

        ResponseEntity<TwitchStreamWrapper> responseEntity = getRestTemplate().exchange(mUri,
                HttpMethod.GET,
                new HttpEntity<Object>(TwitchApi.getHeaders()),
                TwitchStreamWrapper.class);

		return responseEntity.getBody();
	}

	private static URI buildURIQuery(String baseUrl, String channel) {
        UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl + "/" + channel);
        URI uri = uriBuilder.build().toUri();

		return uriBuilder.build().toUri();
	}

	@Override
	public Object getCacheKey() {
		return mUri.toString();
	}
}
