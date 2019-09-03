package br.com.waterclockapp.ui.settings


import android.app.Dialog
import android.os.Bundle
import android.view.*
import android.widget.Button
import androidx.fragment.app.Fragment
import android.widget.TextView
import android.widget.Toast

import br.com.waterclockapp.R
import br.com.waterclockapp.util.DIALOG_MESSAGE_CLEAN_SETTINGS
import br.com.waterclockapp.util.Preferences
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : Fragment() {



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textViewNameSettings.text = Preferences.getPreferences()?.name
        buttonCleanCredencias.setOnClickListener { showDialogChooser() }
    }

    fun showDialogChooser(){
        val dialog: Dialog = Dialog(context, R.style.CustomAlertDialog)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.chooser_dialog)
        dialog.setCancelable(false)
        dialog.findViewById<TextView>(R.id.textViewDialogQuestion).text = DIALOG_MESSAGE_CLEAN_SETTINGS
        dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN)
        dialog.show()

        dialog.findViewById<Button>(R.id.buttonYesChoose).setOnClickListener {
            Preferences.clearPreferences()
            dialog.dismiss()
            Toast.makeText(context, R.string.clear_preferences, Toast.LENGTH_SHORT).show ()
        }

        dialog.findViewById<Button>(R.id.buttonNoChoose).setOnClickListener {
            dialog.dismiss()
        }
    }


}
