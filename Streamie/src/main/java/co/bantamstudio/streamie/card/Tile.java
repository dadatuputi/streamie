package co.bantamstudio.streamie.card;

import android.view.LayoutInflater;
import android.view.View;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.Comparator;

import co.bantamstudio.streamie.HelperUtils;
import co.bantamstudio.streamie.auth.model.Stream;
import lombok.Getter;
import lombok.Setter;

public abstract class Tile implements Comparable<Tile> {
    final HelperUtils.FragmentManager pusher;
    private TileCallback mCallback;
	@Getter protected Stream stream;
	final ImageLoader mImageLoader;
	final DisplayImageOptions mDisplayOptions;
	@Getter @Setter protected boolean isExpanded;

	Tile(Stream stream, HelperUtils.FragmentManager pusher) {
		this.pusher = pusher;
		this.stream = stream;
    	mImageLoader = ImageLoader.getInstance();
		
		mDisplayOptions = new DisplayImageOptions.Builder()
				.resetViewBeforeLoading(true).cacheInMemory(true)
				.imageScaleType(ImageScaleType.EXACTLY)
				.build();

	}

	public abstract int getViewType();
    public abstract View getView(LayoutInflater inflater, View convertView);
    
    public boolean onClick() {
    	if (!isExpanded) {
    		// EXPAND
    		
    		return true;
    	}
    	if (mCallback != null) {
    		mCallback.onClickCallback();
    		return true;
    	} else {
    		return false;
    	}
    	
    }
    
    public void setOnClickCallback(TileCallback callback) {
    	mCallback = callback;
    }
    
    public interface TileCallback {
        void onClickCallback();
    }
    
    @Override
    public boolean equals(Object o) {
    	return (o instanceof Stream) && ((Stream) o).getUsername().equalsIgnoreCase(stream.getUsername()); 
    }
    
    @Override
    public int compareTo(Tile another) {
    	return stream.getViewers() - another.getStream().getViewers();
    }
    
    @Override
    public String toString() {
    	return stream.toString();
    }
    
    public static class ReverseCardComparator implements Comparator<Tile>{

		@Override
		public int compare(Tile lhs, Tile rhs) {
			return rhs.getStream().getViewers() < lhs.getStream().getViewers() ? -1 : 
				rhs.getStream().getViewers() == lhs.getStream().getViewers() ? 0 : 1;
		}
    }
}
