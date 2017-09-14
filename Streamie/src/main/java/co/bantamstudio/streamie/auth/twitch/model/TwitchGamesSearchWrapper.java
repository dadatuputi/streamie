package co.bantamstudio.streamie.auth.twitch.model;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import co.bantamstudio.streamie.auth.model.Category;
import org.springframework.util.CollectionUtils;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TwitchGamesSearchWrapper extends TwitchGamesWrapper {
	@JsonProperty @Getter protected ArrayList<TwitchGame> games;
	
	@JsonIgnore
	@Override
	public List<Category> getCategories() {
		if (CollectionUtils.isEmpty(mCategories))
			mCategories = buildCategories();
			
		return mCategories;	
	}

	@JsonIgnore
	private List<Category> buildCategories(){
		if (CollectionUtils.isEmpty(games)) {
			return null;
		} else {
			// CONVERT SEARCH RESULTS TO CATEGORIES
			List<Category> categories = new ArrayList<Category>();
			for (TwitchGame game : games) {
				categories.add(new TwitchGameWrapper(game));
			}
			return categories;
		}
	}
}
