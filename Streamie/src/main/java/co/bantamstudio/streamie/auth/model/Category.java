package co.bantamstudio.streamie.auth.model;

public interface Category extends Comparable<Category> {
	static final String CATEGORY_TYPE = "category_type";
	static final String CATEGORY_LIMIT = "category_limit";
	static final String SOURCE_TITLE = "source_title";
	String getName();
	String getKey();
	int getViewersCount();
	int getChannelsCount();
	int compareTo(Category another);
	@Override
	public boolean equals(Object o);
}
