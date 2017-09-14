package co.bantamstudio.streamie.justintv.api;

import org.springframework.util.CollectionUtils;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import co.bantamstudio.streamie.api.StreamieRequest;
import co.bantamstudio.streamie.auth.justintv.model.JtvStreamsWrapper;
import co.bantamstudio.streamie.auth.justintv.model.JustinTVStream;

public class JustinTVStreamRequest extends StreamieRequest<JtvStreamsWrapper> {

	private final URI mUri;

	public JustinTVStreamRequest(String category, String language, int limit, int offset) {
		this(category, null, null, language, limit, offset);		
	}
	
	public JustinTVStreamRequest(String category, List<String> channels, String title, String language, int limit, int offset) {
		this(buildURI(category, channels, title, language, limit, offset));
	}
	
	private JustinTVStreamRequest(URI uri) {
		super( JtvStreamsWrapper.class );
		mUri = uri;
	}

	@Override
	public JtvStreamsWrapper loadDataFromNetwork() throws Exception {
		JustinTVStream[] streams = getRestTemplate().getForObject(mUri, JustinTVStream[].class);
		ArrayList<JustinTVStream> newStreams = new ArrayList<JustinTVStream>();
		
		// SKIP GAMING STREAMS
		for (JustinTVStream stream : streams) {
			//if (!stream.getCategory().equalsIgnoreCase("gaming"))
                newStreams.add(stream);
		}
		
		return new JtvStreamsWrapper(newStreams);
	}
	
	
	private static URI buildURI(String category, List<String> channels, String title, String language, int limit, int offset) {
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(JustinTVApi.STREAMLIST);
		
		if (category != null && !category.equalsIgnoreCase("") && !category.equalsIgnoreCase("all"))
			uriBuilder.queryParam("category", category);
		if (!CollectionUtils.isEmpty(channels)) {
            for (String channel : channels)
			    uriBuilder.queryParam("channel", channel);
        }
		if (title != null && !title.equalsIgnoreCase(""))
			uriBuilder.queryParam("title", title);		
		if (language != null && !language.equalsIgnoreCase("") && !language.equalsIgnoreCase("all"))
			uriBuilder.queryParam("language", language);					
		if (limit > 0)
			uriBuilder.queryParam("limit", Integer.toString(limit));	
		if (offset > 0)
			uriBuilder.queryParam("offset", Integer.toString(limit));	
		
		return uriBuilder.build().toUri();
	}
	

	@Override
	public Object getCacheKey() {
		return mUri.toString();
	}
}