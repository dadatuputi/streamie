package co.bantamstudio.streamie.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.octo.android.robospice.SpiceManager;

import java.util.HashSet;
import java.util.Set;

import co.bantamstudio.streamie.StreamieApplication;
import co.bantamstudio.streamie.card.Tile;

public class CardAdapter extends ArrayAdapter<Tile> implements KeywordAdapter {
    private final LayoutInflater mInflater;

	public enum TileType {
		LARGE_TILE, SMALL_TILE
	}

	public CardAdapter(Activity activity,
			SpiceManager spiceManager) {
		super(activity, 0);
		mInflater = LayoutInflater.from(activity);
        StreamieApplication mApplication = (StreamieApplication) activity.getApplication();
	}

	public boolean selectItem(int position) {
		return getItem(position).onClick();
	}
	
    @Override
    public int getViewTypeCount() {
        return TileType.values().length;
    }

    @Override
    public int getItemViewType(int position) {
        return getItem(position).getViewType();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getItem(position).getView(mInflater, convertView);
    }

    @Override
    public Set<String> getKeywords() {
        Set<String> keywords = new HashSet<String>();
        for(int i = 0; i < getCount(); i++){
            keywords.add(getItem(i).getStream().getTitle());
            if (getItem(i).getStream().getGame() != null)
                keywords.add(getItem(i).getStream().getGame());
        }
        return keywords;
    }
}