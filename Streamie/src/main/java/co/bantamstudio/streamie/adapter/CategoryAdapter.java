package co.bantamstudio.streamie.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import co.bantamstudio.streamie.R;
import co.bantamstudio.streamie.StreamieApplication;
import co.bantamstudio.streamie.auth.justintv.model.JtvCategory;
import co.bantamstudio.streamie.auth.model.Category;
import co.bantamstudio.streamie.auth.model.CategoryLoading;
import co.bantamstudio.streamie.auth.twitch.model.TwitchGameWrapper;
import co.bantamstudio.streamie.justintv.api.JustinTVApi;
import co.bantamstudio.streamie.twitch.api.TwitchApi;

public class CategoryAdapter extends ArrayAdapter<Category> implements 
	ListAdapter, 
	KeywordAdapter {

	private class ViewHolder {
		public ImageView image;
		public TextView subText;
		public TextView numViewers;
		public TextView text;
	}

	private final LayoutInflater mInflater;
	private final Context mContext;
	private final StreamieApplication mApplication;
	
	public CategoryAdapter(Context context, int textViewResourceId,	List<Category> objects) {
		super(context, textViewResourceId, objects);
		mContext = context;
		mApplication = (StreamieApplication) context.getApplicationContext();
		mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		final ViewHolder holder;
		
		// CATEGORY LOADING
		if (getItem(position) instanceof CategoryLoading){
			view = mInflater.inflate(R.layout.list_item_loading, null);
			return view;
		}
		
		if (convertView == null || convertView.getTag() == null) {
			view = mInflater.inflate(R.layout.list_item_stream, null);
			holder = new ViewHolder();
			holder.text = (TextView) view.findViewById(R.id.listItemText);
			holder.subText = (TextView) view.findViewById(R.id.listItemSubtext);
			holder.image = (ImageView) view.findViewById(R.id.listItemIcon);
			holder.numViewers = (TextView) view.findViewById(R.id.listItemViewers);
			view.setTag(holder);
		} else
			holder = (ViewHolder) view.getTag();

		Category category = getItem(position);
		
		if (category.getName() != null) {
			holder.text.setText(category.getName());
		}
		
		// POPULATE ICON WITH CORRECT IMAGE
		setImage(category, holder.image, mContext);
		
		if (category.getViewersCount() >= 0) {		
			holder.numViewers.setText(Integer.toString(category.getViewersCount()));
            holder.numViewers.setVisibility(View.VISIBLE);
			
			// SET ICON BASED ON THEME
			String theme = mApplication.getAppThemeString();
			if (theme.equalsIgnoreCase("dark"))
				holder.numViewers.setCompoundDrawablesWithIntrinsicBounds(R.drawable.viewers_inverted, 0, 0, 0);
			
		} else {
			holder.numViewers.setVisibility(View.GONE);
		}

		if (category.getChannelsCount() >= 0) {
			holder.subText.setText(Integer.toString(category.getChannelsCount()) + " Live Channels");
            holder.subText.setVisibility(View.VISIBLE);
		} else {
			holder.subText.setVisibility(View.GONE);
		}

		return view;
	}

	public void setData(List<Category> items) {
		if (items != null){
			clear();
	        for(Category item: items){
	            super.add(item);
	        }
		}
	}
	
	@Override
	public void addAll(Collection<? extends Category> collection) {
		for (Category category : collection) {
			if (!contains(category))
				add(category);
		}
	}
	
	// TODO Optimize this
	private boolean contains(Category category) {
		boolean contained = false;
        for (int i = getCount()-1; i >= 0; i--) {
			if (category.equals(getItem(i)))
				contained = true;
		}
		return contained;
	}

	@Override
	public Set<String> getKeywords() {
		Set<String> keywords = new HashSet<String>();
		for(int i = 0; i < getCount(); i++){
			keywords.add(getItem(i).getName());
		}
		return keywords;
	}
	
	private void setImage(Category category, ImageView image, Context context) {
		if (category instanceof JtvCategory)
			JustinTVApi.setImage((JtvCategory) category, image, mContext);
		else if (category instanceof TwitchGameWrapper)
			TwitchApi.setImage((TwitchGameWrapper) category, image, mContext);
	}
}
