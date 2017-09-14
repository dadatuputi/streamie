package co.bantamstudio.streamie.auth.twitch.api.impl;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.social.support.URIBuilder;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.Collections;

import co.bantamstudio.streamie.auth.twitch.api.UserOperations;
import co.bantamstudio.streamie.auth.twitch.model.TwitchProfile;
import co.bantamstudio.streamie.twitch.api.TwitchApi;

public class UserTemplate extends AbstractTwitchOperations implements UserOperations{

	private final RestTemplate restTemplate;
	
	public UserTemplate(RestTemplate restTemplate, boolean isAuthorized) {
		super(isAuthorized);
		this.restTemplate = restTemplate;
	}

	@Override
	public TwitchProfile getUserProfile() {
		requireAuthorization();
		
		URI url = URIBuilder.fromUri(TwitchApi.USER_SELF).build();
		
		HttpHeaders requestHeaders = new HttpHeaders();
		requestHeaders.setAccept(Collections.singletonList(MediaType.valueOf("application/vnd.twitchtv.v2+json")));
		

		ResponseEntity<TwitchProfile> responseEntity = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(requestHeaders), TwitchProfile.class);
		return responseEntity.getBody();
	}

}
