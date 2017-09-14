package co.bantamstudio.streamie.auth.justintv.api.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import co.bantamstudio.streamie.auth.justintv.api.ChannelOperations;
import co.bantamstudio.streamie.auth.justintv.api.FavoritesOperations;
import co.bantamstudio.streamie.auth.justintv.api.JustinTV;
import co.bantamstudio.streamie.auth.justintv.api.UserOperations;
import org.springframework.social.oauth1.AbstractOAuth1ApiBinding;

public class JustinTVTemplate extends AbstractOAuth1ApiBinding implements JustinTV {

	private UserOperations userOperations;
	private ChannelOperations channelOperations;
	private FavoritesOperations favoritesOperations;

	public JustinTVTemplate(String consumerKey, String consumerSecret, String accessToken, String accessTokenSecret) {
		super(consumerKey, consumerSecret, accessToken, accessTokenSecret);
		initSubApis();
	}

	@Override
	public FavoritesOperations followsOperations() {
		return favoritesOperations;
	}

	@Override
	public UserOperations userOperations() {
		return userOperations;
	}

	@Override
	public ChannelOperations channelOperations() {
		return channelOperations;
	}
	
	private void initSubApis() {
		this.favoritesOperations = new FavoritesTemplate(getRestTemplate(), isAuthorized());
		this.userOperations = new UserTemplate(getRestTemplate(), isAuthorized()); 
	}
	
	@Override
	protected List<HttpMessageConverter<?>> getMessageConverters() {
		MappingJacksonHttpMessageConverter jackson = new MappingJacksonHttpMessageConverter();	
		List<HttpMessageConverter<?>> converters = new ArrayList<HttpMessageConverter<?>>();
		converters.add(jackson);
		return converters;
	}
}
