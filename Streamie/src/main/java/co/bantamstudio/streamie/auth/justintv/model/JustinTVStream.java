package co.bantamstudio.streamie.auth.justintv.model;

import java.io.IOException;

import co.bantamstudio.streamie.HelperUtils;
import co.bantamstudio.streamie.R;
import lombok.Getter;
import lombok.Setter;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import co.bantamstudio.streamie.auth.model.Stream;
import org.springframework.util.StringUtils;

import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import co.bantamstudio.streamie.StreamieApplication;
import co.bantamstudio.streamie.justintv.JustinTVStreamDetailFragment;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JustinTVStream implements Stream {
	@JsonProperty @Getter private int stream_count = -1;	
	@JsonProperty @Getter private String name;	
	@JsonProperty @Getter private int embed_count;	
	@JsonProperty @Getter private int channel_count;	
	@JsonProperty @Getter private String format;	
	@JsonProperty @Getter private String category;
	@JsonProperty @Getter private String subcategory;
	@JsonProperty private String title;	
	@JsonProperty @Getter private JtvChannel channel;
	@JsonProperty @Getter private boolean abuse_reported;
	@JsonProperty @Getter private int site_count;
	@JsonProperty @Getter private boolean featured;
	@JsonProperty @Getter private float video_bitrate;	
	@JsonProperty @Getter private String language;
	@JsonProperty @Getter private int video_height;
	@JsonProperty @Getter private String broadcaster;
	@JsonProperty @Getter private String meta_game;
	@JsonProperty @Getter private String id;
	@JsonProperty @Getter private int channel_view_count;
	@JsonProperty @Getter private String geo;
	@JsonProperty @Getter private int video_width;
	@JsonProperty @Getter private String up_time;
	@JsonProperty @Getter private String audio_codec;
	@JsonProperty @Getter private int broadcast_part;
	@JsonProperty @Getter private String stream_type;
	@JsonProperty @Getter private boolean embed_enabled;
	@JsonProperty @Getter private boolean channel_subscription;
	@JsonProperty @Getter private String video_codec;
	@JsonProperty @Getter @Setter private boolean isOnline = true;
	@JsonProperty @Getter @Setter private String chatEmbed;

	@JsonIgnore
	@Override
	public String toString() {
		return title;
	}

	@JsonIgnore
	@Override
	public int getViewers() {
		return stream_count;
	}

	@JsonIgnore
	@Override
	public String getEmbed() {
		if (getChannel() != null)
			return getChannel().getEmbed_code();
		else
			return null;
	}

	@JsonIgnore
	@Override
	public String getDescription() {
		if (getChannel() != null)
			return getChannel().getAbout();
		else
			return null;
	}

	@JsonIgnore
	@Override
	public String getShortDescription() {
		if (getChannel() != null)
			return getChannel().getDescription();
		else
			return null;
	}

	@JsonIgnore
	@Override
	public String getScreenshot() {
		if (getChannel() != null)
			return getChannel().getScreen_cap_url_huge();
		else
			return null;
	}

	@JsonIgnore
	@Override
	public String getChannelTitle() {
		if (channel != null)
			return channel.getStatus();
		else
			return null;
	}

	@JsonIgnore
	@Override
	public String getNiceUsername() {
		if (channel != null)
			return channel.getTitle();
		else
			return null;
	}

	@JsonIgnore
	@Override
	public String getUsername() {
		if (channel != null)
			return channel.getLogin();
		else
			return name;
	}

	@JsonIgnore
	@Override
	public String getProfileImage() {
		if (channel != null)
			return channel.getImage_url_medium();
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
	public String getTitle() {
		if (channel != null && StringUtils.hasText(channel.getStatus()))
			return channel.getStatus();
		else
			return title;
	}

	@JsonIgnore
	@Override
	public String getGame() {
		return meta_game;
	}

	@JsonIgnore
	@Override
	public String getUrl() {
		if (channel != null)
			return channel.getChannel_url();
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
		if (this.getChannel() != null)
			this.getChannel().setEmbed_code(embed);
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
            bundle.putString(StreamieApplication.TITLE, context.getResources().getString(R.string.justin_tv));
            bundle.putString(StreamieApplication.SUBTITLE, getUsername());
            pusher.pushFragment(JustinTVStreamDetailFragment.class,
                    bundle);
		} else {
			Toast.makeText(context, "Stream is offline", Toast.LENGTH_SHORT).show();
		}
	}

	@JsonIgnore
	@Override
	public String getHls() {
		// Justin.tv doesn't have HLS
		return null;
	}

	@JsonIgnore
	@Override
	public void setHls(String hls) {
		// Justin.tv doesn't have HLS
	}
}
