package co.bantamstudio.streamie.auth.twitch.model;

import java.util.ArrayList;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import co.bantamstudio.streamie.auth.model.Stream;
import co.bantamstudio.streamie.auth.model.StreamsWrapper;
import org.springframework.util.CollectionUtils;

import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TwitchFeaturedStreamsWrapper implements StreamsWrapper {
	@JsonProperty @Getter private ArrayList<TwitchFeaturedStreamWrapper> featured;
	@JsonProperty @Getter private TwitchLinks _links;
	
	@JsonIgnore
	@Override
	public ArrayList<Stream> getStreams() {
		if (CollectionUtils.isEmpty(featured)) {
			return null;
		} else {
			ArrayList<Stream> streams = new ArrayList<Stream>(featured.size());
			for (TwitchFeaturedStreamWrapper wrapper : featured ) {
				TwitchStream stream = wrapper.getStream();
				stream.setDescription(wrapper.getText());
				streams.add(stream);
			}
			return streams;
		}
	}
}
