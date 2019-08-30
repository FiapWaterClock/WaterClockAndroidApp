package br.com.waterclockapp.ui.login

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import br.com.waterclockapp.domain.User
import com.google.android.material.snackbar.Snackbar

class LoginContract {

    interface Presenter {

        fun startLogin(username: String, password: String)

        fun showSnack(view: android.view.View, message: String, length: Int = Snackbar.LENGTH_LONG)



    }

    interface View{
        fun showProgressBar(show: Boolean)

        fun getActivity() : AppCompatActivity

        fun notification(message: String)

        fun saveUserPreferences(user: User)

        fun showSnack(message: String)
    }
}