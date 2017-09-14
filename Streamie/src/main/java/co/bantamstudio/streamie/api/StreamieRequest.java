package co.bantamstudio.streamie.api;

import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.request.springandroid.SpringAndroidSpiceRequest;

import org.springframework.util.Assert;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public abstract class StreamieRequest<RESULT> extends SpringAndroidSpiceRequest<RESULT>  {

	protected StreamieRequest(Class<RESULT> clazz) {
		super(clazz);
	}
	
	public abstract Object getCacheKey();
	
	public long getCacheLength() {
		return DurationInMillis.ONE_MINUTE * 30;
	}
	
	protected static URI buildURI(String url) {
		Assert.hasText(url);
		return UriComponentsBuilder.fromHttpUrl(url).build().toUri();
	}
	
	protected static URI buildURI(String baseUrl, int limit, int offset) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl);
		if (limit > 0)
			uriBuilder.queryParam("limit", Integer.toString(limit));	
		if (offset > 0)
			uriBuilder.queryParam("offset", Integer.toString(limit));	
		
		return uriBuilder.build().toUri();
	}
	
	protected static URI buildSearchURIQuery(String baseUrl, String query, int limit, int offset) {
		Assert.hasText(baseUrl);
		Assert.hasText(query);
		
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(baseUrl);
		
		uriBuilder.queryParam("q", query);
		
		if (limit > 0)
			uriBuilder.queryParam("limit", Integer.toString(limit));	
		if (offset > 0)
			uriBuilder.queryParam("offset", Integer.toString(offset));	
		
		return uriBuilder.build().toUri();
	}
}
