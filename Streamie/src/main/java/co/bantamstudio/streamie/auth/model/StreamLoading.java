package co.bantamstudio.streamie.auth.model;

import android.content.Context;

import co.bantamstudio.streamie.HelperUtils;

public class StreamLoading implements Stream {

	public boolean isOnline() {
		return false;
	}

	@Override
	public String getTitle() {
		return null;
	}

	@Override
	public String getEmbed() {
		return null;
	}

	@Override
	public String getDescription() {
		return null;
	}

	@Override
	public String getShortDescription() {
		return null;
	}

	@Override
	public String getScreenshot() {
		return null;
	}

	@Override
	public int getViewers() {
		return 0;
	}

	@Override
	public Channel getChannel() {
		return null;
	}

	@Override
	public String getChannelTitle() {
		return null;
	}

	@Override
	public String getNiceUsername() {
		return null;
	}

	@Override
	public String getUsername() {
		return null;
	}

	@Override
	public String getProfileImage() {
		return null;
	}

	@Override
	public boolean isMature() {
		return false;
	}

	@Override
	public void setOnline(boolean isOnline) {
		// DO NOTHING
	}

	@Override
	public String getGame() {
		return null;
	}

	@Override
	public String getUrl() {
		return null;
	}

	@Override
	public int compareTo(Stream another) {
		return 0;
	}

	@Override
	public String getError() {
		return null;
	}

	@Override
	public void setEmbed(String embed) {
		// DO NOTHING
	}

	@Override
	public String getChatEmbed() {
		// DO NOTHING
		return null;
	}

	@Override
	public void setChatEmbed(String embed) {
		// DO NOTHING
	}

	@Override
	public void loadStream(Context context, HelperUtils.FragmentManager pusher) {
		// DO NOTHING
	}

	@Override
	public String getHls() {
		// DO NOTHING
		return null;
	}

	@Override
	public void setHls(String hls) {
		// DO NOTHING
		
	}
}