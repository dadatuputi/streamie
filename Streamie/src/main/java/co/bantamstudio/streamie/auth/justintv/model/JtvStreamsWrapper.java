package co.bantamstudio.streamie.auth.justintv.model;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import co.bantamstudio.streamie.auth.model.Stream;
import co.bantamstudio.streamie.auth.model.StreamsWrapper;


import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class JtvStreamsWrapper implements StreamsWrapper {
	@Setter private List<JustinTVStream> streams;
	
	public JtvStreamsWrapper() {
	}
	
	public JtvStreamsWrapper(List<JustinTVStream> streams) {
		this.streams = streams;
	}
	
	@Override
	public ArrayList<Stream> getStreams() {
		return new ArrayList<Stream>(this.streams);
	}
}
