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
public class TwitchStreamsWrapper implements StreamsWrapper {
	@JsonProperty private ArrayList<TwitchStream> streams;
	@JsonProperty @Getter private TwitchLinks _links;
	
	@JsonIgnore
	@Override
	public ArrayList<Stream> getStreams() {
		if (CollectionUtils.isEmpty(streams)) {
			return null;
		} else {
			return new ArrayList<Stream>(this.streams);
		}

	}
}
