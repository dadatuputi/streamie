package co.bantamstudio.streamie.auth.twitch.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;

import co.bantamstudio.streamie.auth.model.Category;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TwitchGameWrapper implements Category {
	public static final String ALL_STREAMS = "All Streams";
	@JsonProperty @Getter private TwitchGame game;
	@JsonProperty @Getter private final int viewers = -1;
	@JsonProperty @Getter private final int channels = -1;
	
	public TwitchGameWrapper(TwitchGame game){
		this.game = game;
	}
	
	public TwitchGameWrapper() {}
	
	@Override
	public boolean equals(Object o) {
		if (o instanceof Category) {
			Category category = (Category) o;
			
			if (category.getName() != null && category.getName().equalsIgnoreCase(this.getName()))
				return true;
		}
		
		return false;
	}

	@Override
	public String toString() {
		return game.getName() + ": " + this.getViewersCount();
	}

	@JsonIgnore
	@Override
	public String getName() {
		return game.getName();
	}

	@JsonIgnore
	@Override
	public String getKey() {
		return game.getName();
	}

	@Override
	public int getViewersCount() {
		return viewers;
	}

	@JsonIgnore
	@Override
	public int getChannelsCount() {
		return channels;
	}

	@Override
	public int compareTo(Category another) {
		if (this.getName().equalsIgnoreCase("all")) {
			return 1;
		} else 
			return this.getViewersCount() - another.getViewersCount();
	}
}