package co.bantamstudio.streamie.drawer;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import org.springframework.util.Assert;

import co.bantamstudio.streamie.R;
import co.bantamstudio.streamie.adapter.DrawerAdapter.RowType;

public class DrawerLoading extends DrawerItem {
	private final String mText1;
	private final String mText2;

	public DrawerLoading(String text1, String text2) {

		Assert.hasText(text1);
		Assert.hasText(text2);
		
		mText1 = text1;
		mText2 = text2;
	}

	@Override
	public int getViewType() {
		return RowType.HEADER_ITEM.ordinal();
	}

	@Override
	public View getView(LayoutInflater inflater, View convertView) {
		View view = inflater.inflate(R.layout.list_item_drawer_loading, null);

		TextView text1 = (TextView) view.findViewById(R.id.text1);
		TextView text2 = (TextView) view.findViewById(R.id.text2);

		text1.setText(mText1);
		text2.setText(mText2);

		return view;
	}
}