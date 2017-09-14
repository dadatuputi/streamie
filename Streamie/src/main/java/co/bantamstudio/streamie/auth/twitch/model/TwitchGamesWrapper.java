package co.bantamstudio.streamie.auth.twitch.model;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonProperty;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

import co.bantamstudio.streamie.auth.model.CategoriesWrapper;
import co.bantamstudio.streamie.auth.model.Category;
import lombok.Getter;

@JsonIgnoreProperties(ignoreUnknown = true)
public class TwitchGamesWrapper implements CategoriesWrapper {
	@JsonProperty @Getter protected int _total;
	@JsonProperty @Getter protected TwitchLinks _links;
	@JsonProperty("top") @Getter
    private List<TwitchGameWrapper> gamesWrapped;
	List<Category> mCategories;
	
	@JsonIgnore
	@Override
	public List<Category> getCategories() {
		if (CollectionUtils.isEmpty(mCategories))
			mCategories = buildCategories();
			
		return mCategories;	
	}

	@JsonIgnore
	private List<Category> buildCategories(){
		if (CollectionUtils.isEmpty(gamesWrapped)) {
			return null;
		} else {
			return new ArrayList<Category>(gamesWrapped);
		}
	}
}
