package co.bantamstudio.streamie.drawer;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.springframework.util.StringUtils;

import co.bantamstudio.streamie.R;
import co.bantamstudio.streamie.adapter.DrawerAdapter.RowType;

public class DrawerListItemImage extends DrawerItem {
	private final String text1;
	private final String text2;
	private final Drawable image;

	public DrawerListItemImage(String text1, String text2, Drawable image) {
		this.text1 = text1;
		this.text2 = text2;
		this.image = image;
	}

	@Override
	public int getViewType() {
		return RowType.HEADER_ITEM.ordinal();
	}

	@Override
	public View getView(LayoutInflater inflater, View convertView) {
		View view = inflater.inflate(R.layout.list_item_drawer_image_two_line, null);

		TextView text1 = (TextView) view.findViewById(R.id.text1);
		TextView text2 = (TextView) view.findViewById(R.id.text2);
		ImageView imageView = (ImageView) view.findViewById(R.id.imageView1);

		// SET IMAGE IF SET
		if (image != null) {
			imageView.setImageDrawable(image);
			imageView.setVisibility(View.VISIBLE);
		} else {
			imageView.setVisibility(View.GONE);
		}
		
		text1.setText(this.text1);
		
		if (StringUtils.hasText(this.text2)) {
			text2.setText(this.text2);
			text2.setVisibility(View.VISIBLE);
		} else {
			text2.setVisibility(View.GONE);
		}

		return view;
	}
}