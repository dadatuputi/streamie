package co.bantamstudio.streamie.auth.model;

import android.content.Context;

import co.bantamstudio.streamie.HelperUtils;

public interface Stream extends Comparable<Stream>{
	String SERIALIZED_JSON = "serialized_json";
	String TYPE = "stream_type";

	public String getChannelTitle();
	public String getNiceUsername();
	public String getUsername();
	public String getProfileImage();
	boolean isOnline();
	void setOnline(boolean isOnline);
	String getTitle();
	String getEmbed();
	void setEmbed(String embed);
	String getDescription();
	String getShortDescription();
	String getScreenshot();
	int getViewers();
	String getHls();
	void setHls(String hls);
	Channel getChannel();
	boolean isMature();
	String getGame();
	public String getUrl();
	String getError();
	String getChatEmbed();
	void setChatEmbed(String embed);
	void loadStream(Context context, HelperUtils.FragmentManager pusher);
	
	public interface OnStreamSelectedListener {
		public void onStreamSelected(Stream stream);
	}
	
	public class EmptyStreamException extends Exception {
		private static final long serialVersionUID = 4269872855187147010L;

		public EmptyStreamException(String string) {
			super(string);
		}
	}
	
	public class StreamOfflineException extends Exception {

		private static final long serialVersionUID = -2467833727550484804L;

		public StreamOfflineException(String string) {
			super(string);
		}
	}	
}
