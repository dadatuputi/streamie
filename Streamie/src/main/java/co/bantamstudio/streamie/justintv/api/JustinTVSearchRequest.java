package co.bantamstudio.streamie.justintv.api;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import co.bantamstudio.streamie.api.StreamieRequest;
import co.bantamstudio.streamie.auth.justintv.model.JtvStreamsWrapper;
import co.bantamstudio.streamie.auth.justintv.model.JustinTVStream;

public class JustinTVSearchRequest extends StreamieRequest<JtvStreamsWrapper> {

	private final URI mUri;
	
	public JustinTVSearchRequest(String query, int limit, int offset) {
		this(buildSearchURIQuery(JustinTVApi.SEARCH, query, limit, offset));
	}
	
	public JustinTVSearchRequest(String url) {
		this(buildURI(url));
	}
	
	private JustinTVSearchRequest(URI uri) {
		super( JtvStreamsWrapper.class );
		Assert.notNull(uri);
		mUri = uri;
	}
	
	protected static URI buildSearchURIQuery(String baseUrl, String query, int limit, int offset) {
		Assert.hasText(baseUrl);
		Assert.hasText(query);
		
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl);
		
		if (limit > 0)
			uriBuilder.queryParam("limit", Integer.toString(limit));	
		if (offset > 0)
			uriBuilder.queryParam("offset", Integer.toString(offset));	
		
		return uriBuilder.buildAndExpand(query).toUri();
	}

	@Override
	public JtvStreamsWrapper loadDataFromNetwork() throws Exception {				
		ResponseEntity<JustinTVStream[]> responseEntity = getRestTemplate().exchange(mUri, HttpMethod.GET, new HttpEntity<Object>(JustinTVApi.getHeaders()), JustinTVStream[].class);
		JustinTVStream[] results = responseEntity.getBody();
		List<JustinTVStream> streams = Arrays.asList(results);
		return new JtvStreamsWrapper(streams);
	}

	@Override
	public Object getCacheKey() {
		return mUri.toString();
	}
}
