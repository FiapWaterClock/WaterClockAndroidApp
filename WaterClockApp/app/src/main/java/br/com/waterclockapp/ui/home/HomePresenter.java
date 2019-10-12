package br.com.waterclockapp.ui.home;

import android.view.MenuItem;

import androidx.fragment.app.Fragment;

import br.com.waterclockapp.R;
import br.com.waterclockapp.ui.camera.CameraFragment;
import br.com.waterclockapp.ui.clock.ClockFragment;
import br.com.waterclockapp.ui.group.GroupFragment;
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
        Fragment fragment = null;
        Fragment currentFragment =  view.getSupportFragmentManager().findFragmentById(R.id.frameHome);
        switch (menuItem.getItemId ()) {
            case R.id.ic_house:
                if(!(currentFragment instanceof InformationFragment))
                    fragment = new InformationFragment(view);

                break;
            case R.id.ic_calendar:
                if(!(currentFragment instanceof HistoricFragment)) {
                    fragment = new HistoricFragment(view);
                }

                break;
            case R.id.ic_bill:
                if(!(currentFragment instanceof PaymentFragment)) {
                    fragment = new PaymentFragment(view);
                }
                break;
            case R.id.ic_camera:
                if(!(currentFragment instanceof CameraFragment)){
                    fragment = new CameraFragment(view);
                }
                view.showFragment(new CameraFragment(view));
                break;
            case R.id.ic_settings:
                if(!(currentFragment instanceof SettingsFragment)){
                    fragment = new  SettingsFragment(view);
                }
                break;
        }
        view.showFragment(fragment);
    }

    @Override
    public void itemClickedAdmin(MenuItem menuItem) {
        Fragment fragment = null;
        Fragment currentFragment =  view.getSupportFragmentManager().findFragmentById(R.id.frameHome);
        switch (menuItem.getItemId ()) {
            case R.id.ic_group:
                if(!(currentFragment instanceof InformationFragment))
                    fragment = new GroupFragment(view);

                break;
            case R.id.ic_clock:
                if(!(currentFragment instanceof HistoricFragment)) {
                    fragment = new ClockFragment(view);
                }

                break;
            case R.id.ic_settings:
                if(!(currentFragment instanceof SettingsFragment)){
                    fragment = new  SettingsFragment(view);
                }
                break;
        }
        view.showFragment(fragment);
    }
}
