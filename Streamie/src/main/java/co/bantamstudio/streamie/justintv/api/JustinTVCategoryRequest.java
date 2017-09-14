package co.bantamstudio.streamie.justintv.api;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import co.bantamstudio.streamie.auth.justintv.model.JtvCategoriesWrapper;

import com.octo.android.robospice.persistence.DurationInMillis;

import co.bantamstudio.streamie.api.StreamieRequest;

public class JustinTVCategoryRequest extends StreamieRequest<JtvCategoriesWrapper> {

	public JustinTVCategoryRequest() {
		super( JtvCategoriesWrapper.class );
	}

	@Override
	public JtvCategoriesWrapper loadDataFromNetwork() throws Exception {
		ResponseEntity<JtvCategoriesWrapper> responseEntity = getRestTemplate().exchange(JustinTVApi.CATEGORIES, HttpMethod.GET, new HttpEntity<Object>(JustinTVApi.getHeaders()), JtvCategoriesWrapper.class);
		return responseEntity.getBody();
	}

	@Override
	public Object getCacheKey() {
		return "jtv_cats";
	}
	
	@Override
	public long getCacheLength() {
		return DurationInMillis.ONE_HOUR;
	}
}