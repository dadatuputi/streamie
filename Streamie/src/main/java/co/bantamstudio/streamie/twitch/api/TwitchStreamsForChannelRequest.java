package co.bantamstudio.streamie.twitch.api;

import com.octo.android.robospice.persistence.DurationInMillis;

import org.apache.commons.lang3.ArrayUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import java.util.Arrays;

import co.bantamstudio.streamie.api.StreamieRequest;
import co.bantamstudio.streamie.auth.twitch.model.TwitchStreamMeta;

public class TwitchStreamsForChannelRequest extends StreamieRequest<TwitchStreamMeta[]>{

	private final String mChannel;
	private final String mSwfUrl;

	public TwitchStreamsForChannelRequest(String channel, String swfUrl) {
		super( TwitchStreamMeta[].class );
		mChannel = channel;
		mSwfUrl = swfUrl;
	}

	@Override
	public Object getCacheKey() {
		return DurationInMillis.ONE_MINUTE*5;
	}

	@Override
	public TwitchStreamMeta[] loadDataFromNetwork() throws Exception {
		ResponseEntity<TwitchStreamMeta[]> response = getRestTemplate().getForEntity(TwitchApi.TWITCH_API, TwitchStreamMeta[].class, mChannel);
		TwitchStreamMeta[] metas = response.getBody();
		
		for (TwitchStreamMeta meta : metas) { 
			String rtmp = meta.getConnect();
			String playPath = meta.getPlay();
			
			// PARSE TOKEN
			if (StringUtils.hasText(meta.getToken())){
				String token = meta.getToken();
				token = StringUtils.replace(token, "\\", "\\5c");
				token = StringUtils.replace(token, " ", "\\20");
				token = StringUtils.replace(token, "\"", "\\22");
				meta.setToken(token);
			}
			
			meta.setSwfUrl(mSwfUrl);
			
			meta.setRtmpPath(String.format("%s/%s swfUrl=%s swfVfy=1 jtv=%s live=1 conn=S:OK", rtmp, playPath, mSwfUrl, meta.getToken()));
			
			meta.setPlayPath("http://www.twitch.tv/" + mChannel);
		}
		
		Arrays.sort(metas);
		ArrayUtils.reverse(metas);
		
		return metas;
	}

}
