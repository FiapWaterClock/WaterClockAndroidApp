package br.com.waterclockapp.ui.settings


import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.waterclockapp.R
import br.com.waterclockapp.data.model.ClockModel
import br.com.waterclockapp.ui.login.LoginActivity
import br.com.waterclockapp.util.CLEAR_SUCCESS_PREFERENCES
import br.com.waterclockapp.util.DELETE_USER
import br.com.waterclockapp.util.DIALOG_MESSAGE_CLEAN_SETTINGS
import br.com.waterclockapp.util.Preferences
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment(), SettingsContract.View{


    private lateinit var presenter: SettingsContract.Presenter

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
        textViewNameSettings.text = Preferences.getPreferences()?.email
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
    }

    private fun goToLogin() {
        val intent = Intent(activity, LoginActivity::class.java)
        startActivity(intent)
        activity?.finish()
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






}
