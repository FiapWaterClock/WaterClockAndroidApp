package br.com.waterclockapp.ui;

import android.view.MenuItem;

import br.com.waterclockapp.R;
import br.com.waterclockapp.ui.information.InformationFragment;
import br.com.waterclockapp.ui.settings.SettingsFragment;

public class HomePresenter implements HomeContract.Presenter{

    private HomeContract.View view;

    public HomePresenter(HomeContract.View view) {
        this.view = view;
    }

    @Override
    public void itemClicked(MenuItem menuItem) {
        switch (menuItem.getItemId ()) {
            case R.mipmap.ic_house:
                view.showFragment (new InformationFragment());
                break;
            case R.mipmap.ic_settings:
                view.showFragment(new SettingsFragment());
                break;
        }
    }
}
