package co.bantamstudio.streamie.auth.twitch.api.impl;

import org.springframework.web.client.RestTemplate;

import co.bantamstudio.streamie.auth.twitch.api.ChannelOperations;

public class ChannelTemplate extends AbstractTwitchOperations implements ChannelOperations{

	private final RestTemplate restTemplate;

	public ChannelTemplate(RestTemplate restTemplate, boolean isAuthorized) {
		super(isAuthorized);
		this.restTemplate = restTemplate;
	}

	@Override
	public String updateStatus(String status, String game) {
		return null;
	}

}
