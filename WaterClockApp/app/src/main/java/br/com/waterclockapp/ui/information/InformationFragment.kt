package br.com.waterclockapp.ui.information


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import br.com.waterclockapp.R
import br.com.waterclockapp.data.model.ConsumptionModel
import br.com.waterclockapp.ui.login.LoginActivity
import br.com.waterclockapp.util.LitersToMoney
import br.com.waterclockapp.util.Preferences
import kotlinx.android.synthetic.main.fragment_information.*
import java.util.*

class InformationFragment : Fragment(), InformationContract.View {


    lateinit var presenter: InformationContract.Presenter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_information, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = InformationPresenter(this)
        val calendar = Calendar.getInstance()
        presenter.getConsumptionMonth(calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR))

    }

    override fun notification(message: String) {
        activity?.let {
            Toast.makeText(it, message , Toast.LENGTH_LONG).show()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun initView(consumptionMonth: ConsumptionModel, consumptionDay: ConsumptionModel) {
        textViewVolumeMonthDay.text = "${(consumptionMonth.litersPerMinute/1000).toString().replace(".",",")}m³"
        textViewValueMonthDay.text = LitersToMoney.convertLitersToMoney(consumptionMonth.litersPerMinute)
        textViewValueDay.text = LitersToMoney.convertDayToMoney(consumptionDay.litersPerMinute, consumptionMonth.litersPerMinute)
        textViewVolumeDay.text = "${(consumptionDay.litersPerMinute/1000).toString().replace(".",",")}m³"
    }

    override fun showProgressRecycler(show: Boolean) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun logout() {
        val intentHome = Intent(activity, LoginActivity::class.java)
        Preferences.clearPreferences()
        startActivity(intentHome)
        activity?.finish()
    }


}
