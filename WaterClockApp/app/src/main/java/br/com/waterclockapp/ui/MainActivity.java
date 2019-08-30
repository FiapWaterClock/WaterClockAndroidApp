package br.com.waterclockapp.ui;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import br.com.waterclockapp.R;
import br.com.waterclockapp.domain.User;

import static br.com.waterclockapp.util.ConstantsKt.USER_ACCOUNT;

public class MainActivity extends AppCompatActivity {

    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadExtras();
    }

    private void loadExtras() {
        user = (User) getIntent().getParcelableExtra(USER_ACCOUNT);
    }
}
