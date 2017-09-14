package co.bantamstudio.streamie.auth.twitch.model;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;

import co.bantamstudio.streamie.HelperUtils;
import co.bantamstudio.streamie.auth.model.Stream;

import co.bantamstudio.streamie.R;
import co.bantamstudio.streamie.StreamieApplication;
import co.bantamstudio.streamie.twitch.TwitchStreamDetailFragment;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;
import lombok.Getter;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TwitchStream implements Stream {
	@JsonProperty @Getter private int viewers = -1;
	@JsonProperty @Getter private long _id;
	@JsonProperty @Getter private String broadcaster;
	@JsonProperty @Getter private String partner;
	@JsonProperty @Getter private String preview;
	@JsonProperty @Getter private TwitchChannel channel;
	@JsonProperty @Getter private String name;
	@JsonProperty @Getter private String game;
	@JsonProperty @Setter private String description;
	@JsonProperty @Getter private TwitchLinks _links;
	@JsonProperty @Getter @Setter private boolean isOnline = true;
	@JsonProperty @Getter @Setter private String chatEmbed;
	@JsonProperty @Getter @Setter private String hls;

	@JsonIgnore
	@Override
	public String getTitle() {
		if (channel != null)
			return channel.getStatus();
		else
			return null;
	}

	@JsonIgnore
	@Override
	public String getDescription() {
		return description;
	}

	@JsonIgnore
	@Override
	public String getShortDescription() {
		if (channel != null)
			return channel.getStatus();
		else
			return null;
	}

	@JsonIgnore
	@Override
	public String getScreenshot() {
		return preview;
	}

	@JsonIgnore
	@Override
	public String getChannelTitle() {
		if (channel != null)
			return channel.getStatus();
		return null;
	}

	@JsonIgnore
	@Override
	public String getNiceUsername() {
		if (channel != null)
			return channel.getDisplay_name();
		else
			return null;
	}

	@JsonIgnore
	@Override
	public String getUsername() {
		if (channel != null)
			return channel.getName();
		else
			return name;
	}

	@JsonIgnore
	@Override
	public String getProfileImage() {
		if (channel != null)
			return channel.getLogo();
		else
			return null;
	}

	@JsonIgnore
	@Override
	public boolean isMature() {
        return channel != null && channel.isMature();
	}

	@JsonIgnore
	@Override
	public String getUrl() {
		if (channel != null)
			return channel.getUrl();
		else
			return null;
	}

	@JsonIgnore
	@Override
	public int compareTo(Stream another) {
		if (another.isOnline() && !this.isOnline)
			return -1;
		else if (this.isOnline && !another.isOnline())
			return 1;
		else
			return this.getViewers() - another.getViewers();
	}

	@JsonIgnore
	@Override
	public String getError() {
		if (channel != null)
			return channel.getError();
		else
			return null;
	}

	@JsonIgnore
	@Override
	public void setEmbed(String embed) {
		if (channel != null)
			channel.setEmbed(embed);
	}

	@JsonIgnore
	@Override
	public String getEmbed() {
		if (channel != null)
			return channel.getEmbed();
		else
			return null;
	}

	@JsonIgnore
	@Override
	public void loadStream(Context context, HelperUtils.FragmentManager pusher) {
		if (isOnline()) {
            Bundle bundle = new Bundle();
            bundle.putString(StreamieApplication.KEY, getUsername());
			
			ObjectMapper mapper = new ObjectMapper();
			String jsonString = "";
			try {
				jsonString = mapper.writeValueAsString(this);
			} catch (JsonGenerationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JsonMappingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            bundle.putString(Stream.SERIALIZED_JSON, jsonString);
            bundle.putString(StreamieApplication.TITLE, context.getResources().getString(R.string.twitch));
            bundle.putString(StreamieApplication.SUBTITLE, getUsername());
            pusher.pushFragment(TwitchStreamDetailFragment.class,
                    bundle);
		} else {
			Toast.makeText(context, "Stream is offline", Toast.LENGTH_SHORT).show();
		}
	}
}
