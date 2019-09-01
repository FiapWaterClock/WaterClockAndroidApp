package br.com.waterclockapp.ui;

import android.view.MenuItem;

import androidx.fragment.app.Fragment;

public class HomeContract {

    interface View{

        void showFragment(Fragment fragment);
    }

    interface Presenter{
        void itemClicked(MenuItem menuItem);
    }
}
