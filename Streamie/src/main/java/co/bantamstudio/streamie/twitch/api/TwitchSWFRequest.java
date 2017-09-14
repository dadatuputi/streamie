package co.bantamstudio.streamie.twitch.api;

import com.octo.android.robospice.persistence.DurationInMillis;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.net.URI;

import co.bantamstudio.streamie.StreamieApplication;
import co.bantamstudio.streamie.api.StreamieRequest;

public class TwitchSWFRequest extends StreamieRequest<URI> {

	private final String mChannel;

	public TwitchSWFRequest(String channel) {
		super( URI.class );
		mChannel = channel;
	}

	@Override
	public Object getCacheKey() {
		return DurationInMillis.ONE_MINUTE * 5;
	}

	@Override
	public URI loadDataFromNetwork() throws Exception {
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setUserAgent(StreamieApplication.STREAMIE_USER_AGENT);
		requestHeaders.add("Referer", "http://www.twitch.tv/" + mChannel);
		
		ResponseEntity<?> responseEntity = getRestTemplate().exchange(TwitchApi.TWITCH_SWF, HttpMethod.HEAD, new HttpEntity<String>(requestHeaders), null, mChannel);
		
		String locationString = responseEntity.getHeaders().getFirst("Location");
		String locationStringEncoded = UriUtils.encodeUri(locationString, "UTF-8");
		return UriComponentsBuilder.fromHttpUrl(locationStringEncoded).build().toUri();
	}
	
}
