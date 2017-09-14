package co.bantamstudio.streamie.drawer;

import android.view.LayoutInflater;
import android.view.View;

public abstract class DrawerItem {

    private DrawerItemCallback mCallback;

	public abstract int getViewType();
    public abstract View getView(LayoutInflater inflater, View convertView);
    
    public boolean onClick() {
    	if (mCallback != null) {
    		mCallback.onClickCallback();
    		return true;
    	} else {
    		return false;
    	}
    }
    
    public void setOnClickCallback(DrawerItemCallback callback) {
    	mCallback = callback;
    }
    
    public interface DrawerItemCallback {
        void onClickCallback();
    }
}

