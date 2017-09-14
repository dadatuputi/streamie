package co.bantamstudio.streamie.auth.justintv.api.impl;

import org.springframework.social.support.URIBuilder;
import org.springframework.web.client.RestTemplate;

import java.net.URI;

import co.bantamstudio.streamie.auth.justintv.api.UserOperations;
import co.bantamstudio.streamie.auth.justintv.model.JustinTVProfile;
import co.bantamstudio.streamie.justintv.api.JustinTVApi;

public class UserTemplate extends AbstractJustinTVOperations implements UserOperations{

	private final RestTemplate restTemplate;
	
	public UserTemplate(RestTemplate restTemplate, boolean isAuthorized) {
		super(isAuthorized);
		this.restTemplate = restTemplate;
	}

	@Override
	public JustinTVProfile getUserProfile() {
		requireAuthorization();
		
		URI url = URIBuilder.fromUri(JustinTVApi.WHOAMI).build();
		
		return restTemplate.getForObject(url, JustinTVProfile.class);
	}

}
