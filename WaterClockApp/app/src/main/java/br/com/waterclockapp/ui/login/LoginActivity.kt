package br.com.waterclockapp.ui.login

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import br.com.waterclockapp.R
import br.com.waterclockapp.domain.User
import br.com.waterclockapp.ui.HomeActivity
import br.com.waterclockapp.util.Preferences
import br.com.waterclockapp.util.USER_ACCOUNT
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(), LoginContract.View {

    private lateinit var presenter:LoginContract.Presenter
    private var shortAnimTime:Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        loadUI()
        loadSaveButton()
        verifyLogin()
    }

    private fun loadSaveButton() {
        buttonLogin.setOnClickListener {
            presenter.startLogin(editTextUsername.text.toString(), editTextPassword.text.toString())
        }
    }

    private fun loadUI() {
        presenter = LoginPresenter(this)
        shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime)
    }

    override fun showProgressBar(show: Boolean) {
        buttonLogin.visibility = if(show) View.INVISIBLE else View.VISIBLE
        progressBarButtonLogin.visibility = if(show) View.VISIBLE else View.GONE
        progressBarButtonLogin.animate().setDuration(shortAnimTime.toLong()).alpha(if(show) 1F else 0F)
                .setListener( object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        progressBarButtonLogin.visibility = if(show) View.VISIBLE else View.GONE
                    }
                })
    }

    override fun getActivity(): AppCompatActivity = this

    private fun goToHome(preferences: User?) {
        if(preferences != null){
            val intent = Intent(this@LoginActivity, HomeActivity::class.java)
            intent.putExtra(USER_ACCOUNT, preferences)
            startActivity(intent)
            finish()
        }
    }

    override fun notification(message: String) {
        Toast.makeText(this@LoginActivity, message, Toast.LENGTH_LONG).show()
    }

    override fun saveUserPreferences(user: User) {
        if(switchLogin.isChecked)
            Preferences.saveUser(user)
        goToHome(user)

    }

    override fun showSnack(message: String) {
        presenter.showSnack(constraintLayoutLogin, message)
    }

    private fun verifyLogin(){
        if (Preferences.isSave()) {
            editTextUsername.setText(Preferences.getPreferences()?.username)
            editTextPassword.setText(Preferences.getPreferences()?.password)
            switchLogin.isChecked = true
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(switchLogin.isChecked)
            Preferences.clearPreferences()
    }
}
