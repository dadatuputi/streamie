package co.bantamstudio.streamie.auth.justintv.model;

import lombok.Getter;
import lombok.Setter;

public class JtvSubCategory {
	@Getter private String name;
	@Getter private int order;
	@Getter private String short_name;
	@Getter @Setter private String key;
}
