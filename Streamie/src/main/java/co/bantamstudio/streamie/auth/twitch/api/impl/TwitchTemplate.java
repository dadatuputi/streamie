package co.bantamstudio.streamie.auth.twitch.api.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.social.oauth2.AbstractOAuth2ApiBinding;
import org.springframework.social.oauth2.OAuth2Version;
import org.springframework.social.support.ClientHttpRequestFactorySelector;
import co.bantamstudio.streamie.auth.twitch.api.ChannelOperations;
import co.bantamstudio.streamie.auth.twitch.api.FollowsOperations;
import co.bantamstudio.streamie.auth.twitch.api.Twitch;
import co.bantamstudio.streamie.auth.twitch.api.UserOperations;
import org.springframework.util.Assert;

public class TwitchTemplate extends AbstractOAuth2ApiBinding implements Twitch {

	private FollowsOperations followsOperations;
	private UserOperations userOperations;
	private ChannelOperations channelOperations;
	protected final static String TWITCH_TYPE = "application";
	protected final static String TWITCH_SUBTYPE = "vnd.twitchtv.v2+json";

	public TwitchTemplate(String accessToken) {
		super(accessToken);
		Assert.notNull(accessToken, "The token must not be null");
		initialize();
	}

	@Override
	public FollowsOperations followsOperations() {
		return followsOperations;
	}

	@Override
	public UserOperations userOperations() {
		return userOperations;
	}

	@Override
	public ChannelOperations channelOperations() {
		return channelOperations;
	}
	
	private void initialize() {
		// Wrap the request factory with a BufferingClientHttpRequestFactory so that the error handler can do repeat reads on the response.getBody()
		super.setRequestFactory(ClientHttpRequestFactorySelector.bufferRequests(getRestTemplate().getRequestFactory()));
		initSubApis();
	}
	
	private void initSubApis() {
		this.followsOperations = new FollowsTemplate(getRestTemplate(), isAuthorized());
		this.userOperations = new UserTemplate(getRestTemplate(), isAuthorized());
	}
	
	@Override
	protected OAuth2Version getOAuth2Version() {
		return OAuth2Version.DRAFT_10;
	}
	
	@Override
	protected List<HttpMessageConverter<?>> getMessageConverters() {
		MappingJacksonHttpMessageConverter jackson = new MappingJacksonHttpMessageConverter();	
		List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
		converters.add(jackson);
		return converters;
	}
}
