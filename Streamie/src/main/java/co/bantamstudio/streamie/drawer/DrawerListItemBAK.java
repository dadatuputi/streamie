package co.bantamstudio.streamie.drawer;
import co.bantamstudio.streamie.R;

import co.bantamstudio.streamie.adapter.DrawerAdapter.RowType;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

public class DrawerListItemBAK extends DrawerItem {
	private final String str1;
	private final String str2;

	public DrawerListItemBAK(String text1, String text2) {
		this.str1 = text1;
		this.str2 = text2;
	}

	@Override
	public int getViewType() {
		return RowType.LIST_ITEM.ordinal();
	}

	@Override
	public View getView(LayoutInflater inflater, View convertView) {
		View view;
		if (convertView == null) {
			view = inflater.inflate(R.layout.list_item_drawer, null);
			// Do some initialization
		} else {
			view = convertView;
		}

		TextView text1 = (TextView) view.findViewById(R.id.text1);
		TextView text2 = (TextView) view.findViewById(R.id.text2);
		text1.setText(str1);
		
		if (str1 != null)
			text2.setText(str2);
		else
			text2.setVisibility(View.GONE);

		return view;
	}
}