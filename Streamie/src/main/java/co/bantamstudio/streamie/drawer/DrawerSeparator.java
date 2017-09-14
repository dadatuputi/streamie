package co.bantamstudio.streamie.drawer;
import co.bantamstudio.streamie.R;

import android.view.LayoutInflater;
import android.view.View;
import co.bantamstudio.streamie.adapter.DrawerAdapter.RowType;

public class DrawerSeparator extends DrawerItem {
    public DrawerSeparator() {
    }

    @Override
    public int getViewType() {
        return RowType.HEADER_ITEM.ordinal();
    }

    @Override
    public View getView(LayoutInflater inflater, View convertView) {
        return inflater.inflate(R.layout.list_item_drawer_separator, null);
    }
}