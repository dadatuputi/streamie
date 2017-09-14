package co.bantamstudio.streamie.auth.twitch.api.impl;

import java.net.URI;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import co.bantamstudio.streamie.auth.twitch.api.FollowsOperations;
import co.bantamstudio.streamie.auth.twitch.model.TwitchChannelWrapper;
import co.bantamstudio.streamie.auth.twitch.model.TwitchStreamsWrapper;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import co.bantamstudio.streamie.twitch.api.TwitchApi;

public class FollowsTemplate extends AbstractTwitchOperations implements FollowsOperations {

	private final RestTemplate restTemplate;
	
	public FollowsTemplate(RestTemplate restTemplate, boolean isAuthorizedForUser) {
		super(isAuthorizedForUser);
		this.restTemplate = restTemplate;
	}
	
	
	
	@Override
	public TwitchStreamsWrapper getFollows(URI uri) {
		requireAuthorization();

		ResponseEntity<TwitchStreamsWrapper> responseEntity = restTemplate.exchange(uri, HttpMethod.GET, new HttpEntity<Object>(TwitchApi.getHeaders()), TwitchStreamsWrapper.class);
		return responseEntity.getBody();
	}
	
	@Override
	public TwitchStreamsWrapper getFollows(int limit, int offset) {
		
		UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromHttpUrl(TwitchApi.FOLLOWS);
		if (limit > 0)
			uriBuilder.queryParam("limit", Integer.toString(limit));	
		if (offset > 0)
			uriBuilder.queryParam("offset", Integer.toString(limit));	
		
		return getFollows(uriBuilder.build().toUri());
	}
	
	@Override
	public TwitchStreamsWrapper getFollows(String url) {
		Assert.hasText(url);	
		return getFollows(UriComponentsBuilder.fromHttpUrl(url).build().toUri());
	}

	
	
	@Override
	public TwitchChannelWrapper followChannel(String user, String channel) {
		requireAuthorization();

		ResponseEntity<TwitchChannelWrapper> responseEntity = restTemplate.exchange(TwitchApi.FOLLOWS_UPDATE, HttpMethod.PUT, new HttpEntity<Object>(TwitchApi.getHeaders()), TwitchChannelWrapper.class, user, channel);
		return responseEntity.getBody();
	}

	@Override
	public String unFollowChannel(String user, String channel) {
		requireAuthorization();
		
		ResponseEntity<String> responseEntity = restTemplate.exchange(TwitchApi.FOLLOWS_UPDATE, HttpMethod.DELETE, new HttpEntity<Object>(TwitchApi.getHeaders()), String.class, user, channel);
		return responseEntity.getBody();
	}
}
