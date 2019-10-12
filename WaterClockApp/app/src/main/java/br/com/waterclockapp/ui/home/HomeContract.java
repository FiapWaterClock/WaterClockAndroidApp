package br.com.waterclockapp.ui.home;

import android.view.MenuItem;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

public class HomeContract {

    public interface View extends HomeDefault.View{

        void showFragment(Fragment fragment) ;

        FragmentManager getSupportFragmentManager();
    }

    public interface Presenter{
        void itemClicked(MenuItem menuItem);

        void itemClickedAdmin(MenuItem menuItem);
    }
}
