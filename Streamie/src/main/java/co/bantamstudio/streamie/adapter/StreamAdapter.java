package co.bantamstudio.streamie.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.springframework.util.Assert;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import co.bantamstudio.streamie.R;
import co.bantamstudio.streamie.StreamieApplication;
import co.bantamstudio.streamie.auth.model.Stream;
import co.bantamstudio.streamie.auth.model.StreamLoading;
import co.bantamstudio.streamie.auth.twitch.model.TwitchStream;

public class StreamAdapter extends ArrayAdapter<Stream> implements 
	ListAdapter,
	KeywordAdapter {
	private final LayoutInflater mInflater;
	private final StreamieApplication mApplication;
	private final int mLayoutID;
	private final Context mContext;

	public StreamAdapter(Context context, int resource, int textViewResourceId,	List<Stream> objects) {	
		super(context, resource, textViewResourceId, objects);
		this.mLayoutID = resource;
		this.mContext = context;
		mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.mApplication = (StreamieApplication) context.getApplicationContext();
	}

	private class ViewHolder {
		public TextView title;
		public TextView numViewers;
		public ImageView image;
		public TextView channel;
		public TextView game;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = convertView;
		final ViewHolder holder;
		
		// CATEGORY LOADING
		if (getItem(position) instanceof StreamLoading){
			view = mInflater.inflate(R.layout.list_item_loading, null);
			return view;
		}


		if (convertView == null || convertView.getTag() == null) {
			view = mInflater.inflate(this.mLayoutID, null);
            Assert.notNull(view);
			holder = new ViewHolder();
			holder.title = (TextView) view.findViewById(R.id.listItemText);
			holder.numViewers = (TextView) view.findViewById(R.id.listItemViewers);
			holder.image = (ImageView) view.findViewById(R.id.listItemIcon);
			holder.channel = (TextView) view.findViewById(R.id.listItemSubtext);
			holder.game = (TextView) view.findViewById(R.id.listItemGame);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
        }

		Stream stream = getItem(position);
		
		// SET STREAM TITLE
		if (stream.getTitle() != null)
			holder.title.setText(stream.getTitle());
		if (stream.getViewers() >= 0) {
			// SHOW NUMBER OF VIEWERS
			holder.numViewers.setText(Integer.toString(stream.getViewers()));
            holder.numViewers.setVisibility(View.VISIBLE);
			// SET ICON BASED ON THEME
			if (mApplication.isDark())
				holder.numViewers.setCompoundDrawablesWithIntrinsicBounds(
						R.drawable.viewers_inverted, 0, 0, 0);
		} else {
			holder.numViewers.setVisibility(View.GONE);
		}
		
		// SET STREAM IMAGE
		setStreamImage(stream, holder.image);
		
		// IS STREAM OFFLINE?
		if (!stream.isOnline()) {
			holder.title.setTypeface(null, Typeface.ITALIC);
			holder.title.setText("Offline: " + holder.title.getText());

		} else {
			holder.title.setTypeface(null, Typeface.NORMAL);
		}
		
		// SHOW GAME TITLE IF GAME CHANNEL
		//if (stream.getGame() != null && (stream instanceof JustinTVStream)){
        if (stream.getGame() != null){
			holder.game.setText(getItem(position).getGame());
			holder.game.setVisibility(View.VISIBLE);
		} else {
			holder.game.setVisibility(View.GONE);
		}
		
		// SHOW CHANNEL
		if (stream.getTitle() != null)
			holder.channel.setText("on " + stream.getTitle() + 
					(!stream.getTitle().equalsIgnoreCase(stream.getUsername())?
							(" (" + stream.getUsername() +")"):
								""));
		else {
			holder.channel.setText("on " + stream.getUsername());
		}

		return view;
	}

	public void setData(List<Stream> items) {
		if (items != null) {
			clear();
			for (Stream item : items) {
				super.add(item);
			}
		}
	}

	@Override
	public Set<String> getKeywords() {
		Set<String> keywords = new HashSet<String>();
		for(int i = 0; i < getCount(); i++){
            if (getItem(i) instanceof TwitchStream && getItem(i).getGame() != null)
                keywords.add(getItem(i).getGame());
            else
                keywords.add(getItem(i).getTitle());
        }
		return keywords;
	}
	
	private void setStreamImage(Stream stream, ImageView image) {
		if (stream.getScreenshot() != null)
			StreamieApplication.getImageLoader().displayImage(stream.getScreenshot(), image, StreamieApplication.getDisplayImageOptions());
	}	
}
