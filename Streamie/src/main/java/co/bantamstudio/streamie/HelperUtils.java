package co.bantamstudio.streamie;

import android.os.Bundle;
import android.support.v4.app.Fragment;

public class HelperUtils {

    public interface FragmentManager {
        void pushFragment(Class<? extends Fragment> fragmentClass, Bundle bundle);
        void popFragment(Fragment fragment);
    }

    public interface DrawerHandler {
        void openDrawer();
        void closeDrawer();
        void refreshDrawer();
    }
}
