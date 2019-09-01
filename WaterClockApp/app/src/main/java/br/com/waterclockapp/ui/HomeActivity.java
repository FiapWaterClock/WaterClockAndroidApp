package br.com.waterclockapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import br.com.waterclockapp.R;
import br.com.waterclockapp.domain.User;

import static br.com.waterclockapp.util.ConstantsKt.USER_ACCOUNT;

public class HomeActivity extends AppCompatActivity implements HomeContract.View{

    private User user;
    private FrameLayout frameHome;
    private BottomNavigationView navigationView;
    private HomeContract.Presenter presenter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        loadUI();

        loadExtras();
        navigationView.setOnNavigationItemSelectedListener(menuitem -> {
            presenter.itemClicked(menuitem);
            return true;
        });

    }

    private void loadUI() {
        presenter = new HomePresenter(this);
        frameHome = findViewById(R.id.frameHome);
        navigationView = findViewById(R.id.bottomNavigationViewHome);
    }

    @Override
    public void showFragment(Fragment fragment){
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frameHome, fragment)
                .commit();
    }

    public void enableNavigation(boolean key) {

        for (int i = 0; i < navigationView.getMenu ().size (); i++) {
            navigationView.getMenu ().getItem (i).setEnabled (!key);
        }
    }

    private void loadExtras() {
        user = (User) getIntent().getParcelableExtra(USER_ACCOUNT);
    }
}
