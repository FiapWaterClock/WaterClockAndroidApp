package br.com.waterclockapp.ui;

import android.view.MenuItem;

import br.com.waterclockapp.R;
import br.com.waterclockapp.ui.camera.CameraFragment;
import br.com.waterclockapp.ui.historic.HistoricFragment;
import br.com.waterclockapp.ui.information.InformationFragment;
import br.com.waterclockapp.ui.payment.PaymentFragment;
import br.com.waterclockapp.ui.settings.SettingsFragment;

public class HomePresenter implements HomeContract.Presenter{

    private HomeContract.View view;

    public HomePresenter(HomeContract.View view) {
        this.view = view;
    }

    @Override
    public void itemClicked(MenuItem menuItem) {
        switch (menuItem.getItemId ()) {
            case R.id.ic_house:
                view.showFragment (new InformationFragment());
                break;
            case R.id.ic_calendar:
                view.showFragment(new HistoricFragment());
                break;
            case R.id.ic_bill:
                view.showFragment(new PaymentFragment());
                break;
            case R.id.ic_camera:
                view.showFragment(new CameraFragment());
                break;
            case R.id.ic_settings:
                view.showFragment(new SettingsFragment());
                break;
        }
    }
}
