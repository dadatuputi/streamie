package co.bantamstudio.streamie.drawer;
import co.bantamstudio.streamie.R;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import co.bantamstudio.streamie.adapter.DrawerAdapter.RowType;

public class DrawerHeader extends DrawerItem {
    private final String name;
    private final Drawable image;

    public DrawerHeader(String name, Drawable image) {
        this.name = "";
        this.image = image;
    }

    @Override
    public int getViewType() {
        return RowType.HEADER_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
    	View view = inflater.inflate(R.layout.list_item_drawer_header, null);

        TextView text = (TextView) view.findViewById(R.id.text1);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageView1);
        
        if (name != null)
        	text.setText(name);
        else
        	text.setVisibility(View.GONE);
        
        // SET IMAGE IF SET
        if (image != null) {
        	imageView.setImageDrawable(image);
        } else {
        	imageView.setVisibility(View.GONE);
        }
        
        return view;
    }
}