package co.bantamstudio.streamie.auth.justintv.model;

import org.codehaus.jackson.annotate.JsonAnySetter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import co.bantamstudio.streamie.auth.model.CategoriesWrapper;
import co.bantamstudio.streamie.auth.model.Category;


public class JtvCategoriesWrapper implements CategoriesWrapper {

	private final Map<String, JtvCategory> categoriesMap = new HashMap<String, JtvCategory>();
	
	@Override
	public ArrayList<Category> getCategories() {
		
		ArrayList<Category> list = new ArrayList<Category>();
		
		for (Map.Entry<String, JtvCategory> entry : categoriesMap.entrySet()) {
			JtvCategory temp = entry.getValue();
			temp.setKey(entry.getKey());
			list.add(temp);
		}
		
		return list;
	}
	
	@JsonAnySetter
	public void anySet(String key, JtvCategory value) {
		categoriesMap.put(key, value);
	}

}
