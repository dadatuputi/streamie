package co.bantamstudio.streamie.auth.justintv.api.impl;

import org.springframework.web.client.RestTemplate;

import co.bantamstudio.streamie.auth.twitch.api.ChannelOperations;

public class ChannelTemplate extends AbstractJustinTVOperations implements ChannelOperations{

	private final RestTemplate restTemplate;

	public ChannelTemplate(RestTemplate restTemplate, boolean isAuthorized) {
		super(isAuthorized);
		this.restTemplate = restTemplate;
	}

	@Override
	public String updateStatus(String status, String title) {
		return null;
	}

}
