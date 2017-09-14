package co.bantamstudio.streamie.auth.justintv.model;

import co.bantamstudio.streamie.auth.model.Category;
import lombok.Getter;
import lombok.Setter;

public class JtvCategory implements Category {
	
	/** The Constant ALL_CATEGORIES. */
	public static final String ALL = "all";

	@Getter @Setter private String key;
	@Getter private String name;
	@Getter private int channel_count;
	@Getter private String icon;
	@Getter private int order;
	@Getter private int viewers_count;
	@Getter private JtvSubCategories subcategories;

	public JtvCategory() {
	}

	public JtvCategory(String key, String name, int channel_count,
			int viewers_count) {
		this.key = key;
		this.name = name;
		this.channel_count = channel_count;
		this.viewers_count = viewers_count;
	}

	@Override
	public int compareTo(Category another) {
		return this.getViewersCount() - another.getViewersCount();
	}

	@Override
	public String toString() {
		return this.name + ": " + this.viewers_count;
	}


	
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
	public int getViewersCount() {
		return viewers_count;
	}

	@Override
	public int getChannelsCount() {
		return channel_count;
	}
}
