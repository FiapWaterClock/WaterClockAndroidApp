package br.com.waterclockapp.ui.settings


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.waterclockapp.R
import br.com.waterclockapp.data.model.ClockModel
import br.com.waterclockapp.data.model.UserModel
import br.com.waterclockapp.ui.home.HomeContract
import br.com.waterclockapp.ui.login.LoginActivity
import br.com.waterclockapp.util.CLEAR_SUCCESS_PREFERENCES
import br.com.waterclockapp.util.DELETE_USER
import br.com.waterclockapp.util.DIALOG_MESSAGE_CLEAN_SETTINGS
import br.com.waterclockapp.util.Preferences
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment(val viewHome: HomeContract.View) : Fragment(), SettingsContract.View{


    private lateinit var presenter: SettingsContract.Presenter
    private var shortAnimTime:Int = 0
    private var userModel: UserModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadUI()
        presenter.getUserInformation()
    }

    private fun loadUI() {
        presenter = SettingsPresenter(this)
        shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime)
        textViewNameSettings.text = Preferences.getPreferences()?.name ?: ""
        buttonCleanCredencias.setOnClickListener {
            showDialogChooser(DIALOG_MESSAGE_CLEAN_SETTINGS) {
                Preferences.clearPreferences()
                Toast.makeText(context, CLEAR_SUCCESS_PREFERENCES, Toast.LENGTH_SHORT).show()
            }
        }
        buttonExit.setOnClickListener { goToLogin() }
        buttonDelete.setOnClickListener {
            showDialogChooser(DELETE_USER) { presenter.deleteUser() }
        }

        buttonUodateProfile.setOnClickListener {
            showDialogUpdate()
        }
    }


    private fun goToLogin() {
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }

    private fun showDialogUpdate(){
        val dialog: Dialog = Dialog(context, R.style.CustomAlertDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.update_profile_dialog)
        dialog.setCancelable(false)
        val firstName = dialog.findViewById<EditText>(R.id.editTextUpdateFirstName)
        val lastName = dialog.findViewById<EditText>(R.id.editTextUpdateLastName)
        val email = dialog.findViewById<EditText>(R.id.editTextUpdateEmail)
        val address = dialog.findViewById<EditText>(R.id.editTextUpdateAddress)
        dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        dialog.show()

        dialog.findViewById<Button>(R.id.buttonUpdateChoose).setOnClickListener {
            userModel = UserModel(userModel?.userId ?: 0,  firstName.text.toString(),
                    lastName.text.toString(),
                    email.text.toString(),
                    address.text.toString(),
                    userModel?.enabled ?: false, userModel?.tokenExpired ?: false, userModel?.clock ?: listOf())
            userModel?.let { user ->  presenter.updateProfile(user)}
            dialog.dismiss()
        }

        dialog.findViewById<Button>(R.id.buttonCancelChoose).setOnClickListener {
            dialog.dismiss()
        }
    }

    fun showDialogChooser(message: String, function: () -> Unit) {
        val dialog: Dialog = Dialog(context, R.style.CustomAlertDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.chooser_dialog)
        dialog.setCancelable(false)
        dialog.findViewById<TextView>(R.id.textViewDialogQuestion).text = message
        dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        dialog.show()

        dialog.findViewById<Button>(R.id.buttonYesChoose).setOnClickListener {
            function()
            dialog.dismiss()

        }

        dialog.findViewById<Button>(R.id.buttonNoChoose).setOnClickListener {
            dialog.dismiss()
        }
    }

    override fun notification(message: String) {
        activity?.let {
            Toast.makeText(it, message , Toast.LENGTH_LONG).show()
        }
    }

    override fun initRecyclerView(models: List<ClockModel>) {
        recyclerViewClock.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = ClockAdapter(models)
        }
    }

    override fun showProgressRecycler(show: Boolean) {
        constraintLayoutSettings.visibility = if(show) View.INVISIBLE else View.VISIBLE
        enabledNavigation(show)
        progressBarSettings.visibility = if(show) View.VISIBLE else View.GONE
        progressBarSettings.animate().setDuration(shortAnimTime.toLong()).alpha(if(show) 1F else 0F)
                .setListener( object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        progressBarSettings.visibility = if(show) View.VISIBLE else View.GONE
                    }
                })
    }

    override fun logout() {
        val intentHome = Intent(activity, LoginActivity::class.java)
        Preferences.clearPreferences()
        startActivity(intentHome)
        activity?.finish()
    }

    override fun getContext(): Context? {
        return super.getContext()
    }

    override fun enabledNavigation(key: Boolean) {
        viewHome.enableNavigation(key)
    }




}
