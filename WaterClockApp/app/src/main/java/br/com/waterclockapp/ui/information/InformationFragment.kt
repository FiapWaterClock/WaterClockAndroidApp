package br.com.waterclockapp.ui.information


import android.animation.Animator
import android.animation.AnimatorListenerAdapter
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
import br.com.waterclockapp.data.model.RateModel
import br.com.waterclockapp.ui.home.HomeContract
import br.com.waterclockapp.ui.login.LoginActivity
import br.com.waterclockapp.util.LitersToMoney
import br.com.waterclockapp.util.Preferences
import kotlinx.android.synthetic.main.fragment_information.*
import java.util.*
import java.text.NumberFormat


class InformationFragment(val viewHome: HomeContract.View) : Fragment(), InformationContract.View {


    lateinit var presenter: InformationContract.Presenter
    private var shortAnimTime:Int = 0

    var formatter = NumberFormat.getCurrencyInstance()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(br.com.waterclockapp.R.layout.fragment_information, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = InformationPresenter(this)
        shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime)
        val calendar = Calendar.getInstance()
        presenter.getConsumptionMonth(calendar.get(Calendar.MONTH) + 1, calendar.get(Calendar.YEAR))

    }

    override fun notification(message: String) {
        activity?.let {
            Toast.makeText(it, message , Toast.LENGTH_LONG).show()
        }
    }

    @SuppressLint("SetTextI18n")
    override fun initView(consumptionMonth: ConsumptionModel, consumptionDay: ConsumptionModel, rate: RateModel) {
        textViewVolumeMonthDay.text = "${(consumptionMonth.litersPerMinute/1000).toString().replace(".",",")} m³"
        textViewValueMonthDay.text = formatter.format(rate.price).toString().replace(".",",")
        textViewValueDay.text = formatter.format((consumptionDay.litersPerMinute/1000) * rate.appliedRate).toString().replace(".",",")
        textViewVolumeDay.text = "${(consumptionDay.litersPerMinute/1000).toString().replace(".",",")} m³"
    }

    override fun showProgressRecycler(show: Boolean) {
        textViewTitleWaterClock.visibility = if(show) View.INVISIBLE else View.VISIBLE
        constraintLayoutValueMensal.visibility = if(show) View.INVISIBLE else View.VISIBLE
        constraintLayoutValueDiario.visibility = if(show) View.INVISIBLE else View.VISIBLE
        progressBarInformation.visibility = if(show) View.VISIBLE else View.GONE
        enabledNavigation(show)
        progressBarInformation.animate().setDuration(shortAnimTime.toLong()).alpha(if(show) 1F else 0F)
                .setListener( object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        progressBarInformation.visibility = if(show) View.VISIBLE else View.GONE
                    }
                })
    }

    override fun logout() {
        val intentHome = Intent(activity, LoginActivity::class.java)
        Preferences.clearPreferences()
        startActivity(intentHome)
        activity?.finish()
    }

    override fun enabledNavigation(key: Boolean) {
        viewHome.enableNavigation(key)
    }


}
