package br.com.waterclockapp.ui.home;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import br.com.waterclockapp.R;
import br.com.waterclockapp.domain.User;
import br.com.waterclockapp.ui.group.GroupFragment;
import br.com.waterclockapp.ui.information.InformationFragment;
import br.com.waterclockapp.util.Preferences;
import br.com.waterclockapp.util.Rebember;

public class HomeActivity extends AppCompatActivity implements HomeContract.View, HomeDefault.View{

    private User user;
    private FrameLayout frameHome;
    private BottomNavigationView navigationView;
    private BottomNavigationView navigationViewAdmin;
    private HomeContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Boolean admin = Preferences.INSTANCE.getPreferences().isAdmin();
        loadAllNavigation(admin == null ? false : admin);


    }

    private void loadAllNavigation(boolean admin){
        loadUI();
        if(admin){
            navigationViewAdmin.setVisibility(View.VISIBLE);
            navigationView.setVisibility(View.INVISIBLE);
            showFragment(new GroupFragment(this));
            navigationViewAdmin.setOnNavigationItemSelectedListener(menuitem -> {
                presenter.itemClicked(menuitem);
                return true;
            });
        }else {
            navigationViewAdmin.setVisibility(View.INVISIBLE);
            navigationView.setVisibility(View.VISIBLE);
            showFragment(new InformationFragment(this));
            navigationView.setOnNavigationItemSelectedListener(menuitem -> {
                presenter.itemClicked(menuitem);
                return true;
            });
        }
    }

    private void loadUI() {
        presenter = new HomePresenter(this);
        frameHome = findViewById(R.id.frameHome);
        navigationView = findViewById(R.id.bottomNavigationViewHome);
        navigationViewAdmin = findViewById(R.id.bottomNavigationViewHomeAdmin);
    }

    @Override
    public void showFragment(Fragment fragment){
        if(fragment != null){
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frameHome, fragment)
                    .commit();
        }

    }

    @Override
    public void enableNavigation(boolean key) {
        for (int i = 0; i < navigationView.getMenu ().size (); i++) {
            navigationView.getMenu ().getItem (i).setEnabled (!key);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(Preferences.INSTANCE.getPreferencesRemember().equals(Rebember.CLOSE))
            Preferences.INSTANCE.clearPreferences();
    }
}
