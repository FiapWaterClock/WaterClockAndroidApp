package br.com.waterclockapp.ui.register

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import br.com.waterclockapp.R
import br.com.waterclockapp.data.model.RegisterModel
import br.com.waterclockapp.domain.User
import br.com.waterclockapp.ui.login.LoginActivity
import br.com.waterclockapp.util.Preferences
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity(), RegisterContract.View{

    private var shortAnimTime:Int = 0
    private lateinit var presenter: RegisterPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        loadUI()
        saveNewRegister()
    }

    private fun saveNewRegister() {
        buttonRegister.setOnClickListener {
            val register = RegisterModel(editTextFirstName.text.toString(), editTextLastName.text.toString(),
                    editTextEmail.text.toString(), editTextPassword.text.toString(), editTextMatchingPassword.text.toString())
            presenter.createNewUser(register)
        }
    }

    private fun loadUI() {
        presenter = RegisterPresenter(this)
        shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime)
    }

    override fun notification(message: String) {
        Toast.makeText(this@RegisterActivity, message, Toast.LENGTH_LONG).show()
    }

    override fun goToHome(user: User) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showProgressBar(show: Boolean) {
        buttonRegister.visibility = if(show) View.INVISIBLE else View.VISIBLE
        progressBarRegister.visibility = if(show) View.VISIBLE else View.GONE
        progressBarRegister.animate().setDuration(shortAnimTime.toLong()).alpha(if(show) 1F else 0F)
                .setListener( object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        progressBarRegister.visibility = if(show) View.VISIBLE else View.GONE
                    }
                })
    }

    override fun getActivity(): AppCompatActivity = this

    override fun logout() {
        val intentHome = Intent(this@RegisterActivity, LoginActivity::class.java)
        Preferences.clearPreferences()
        startActivity(intentHome)
        finish()
    }
}
