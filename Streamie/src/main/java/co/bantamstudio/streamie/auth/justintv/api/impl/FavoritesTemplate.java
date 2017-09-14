package co.bantamstudio.streamie.auth.justintv.api.impl;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import co.bantamstudio.streamie.auth.justintv.api.FavoritesOperations;
import co.bantamstudio.streamie.auth.justintv.model.JustinTVProfile;
import org.springframework.web.client.RestTemplate;

import co.bantamstudio.streamie.justintv.api.JustinTVApi;

public class FavoritesTemplate extends AbstractJustinTVOperations implements FavoritesOperations {

	private final RestTemplate restTemplate;
	
	public FavoritesTemplate(RestTemplate restTemplate, boolean isAuthorizedForUser) {
		super(isAuthorizedForUser);
		this.restTemplate = restTemplate;
	}	
	
	@Override
	public JustinTVProfile followChannel(String channel) {
		requireAuthorization();
		
		ResponseEntity<JustinTVProfile> responseEntity = restTemplate.exchange(JustinTVApi.FOLLOWS_CREATE, HttpMethod.PUT, new HttpEntity<Object>(JustinTVApi.getHeaders()), JustinTVProfile.class, channel);
		return responseEntity.getBody();
	}

	@Override
	public String unFollowChannel(String channel) {
		requireAuthorization();
		
		ResponseEntity<String> responseEntity = restTemplate.exchange(JustinTVApi.FOLLOWS_CREATE, HttpMethod.DELETE, new HttpEntity<Object>(JustinTVApi.getHeaders()), String.class, channel);
		return responseEntity.getBody();
	}


}
