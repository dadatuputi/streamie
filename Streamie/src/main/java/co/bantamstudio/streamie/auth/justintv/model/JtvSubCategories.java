package co.bantamstudio.streamie.auth.justintv.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.codehaus.jackson.annotate.JsonAnySetter;

import lombok.Getter;

public class JtvSubCategories {
	@Getter Map<String, JtvSubCategory> subCategoriesMap = new HashMap<String, JtvSubCategory>(); 
	
	public ArrayList<JtvSubCategory> getSubCategories() {
		
		ArrayList<JtvSubCategory> list = new ArrayList<JtvSubCategory>();
		
		for (Map.Entry<String, JtvSubCategory> entry : subCategoriesMap.entrySet()) {
			JtvSubCategory temp = entry.getValue();
			temp.setKey(entry.getKey());
			list.add(temp);
		}
		
		return list;
	}

	@JsonAnySetter
	public void anySet(String key, JtvSubCategory value) {
		subCategoriesMap.put(key, value);
	}
}
