package br.com.waterclockapp.ui.historic.days


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import br.com.waterclockapp.R
import br.com.waterclockapp.data.model.ConsumptionModel
import br.com.waterclockapp.ui.historic.HistoricContract
import br.com.waterclockapp.ui.historic.HistoricPresenter
import br.com.waterclockapp.ui.home.HomeContract
import br.com.waterclockapp.ui.login.LoginActivity
import br.com.waterclockapp.util.Preferences
import kotlinx.android.synthetic.main.fragment_days.*

class DaysFragment(var month: Int, var year: Int, val viewHome: HomeContract.View) : Fragment(), HistoricContract.View {


    private lateinit var presenter: HistoricContract.Presenter
    private var shortAnimTime:Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_days, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = HistoricPresenter(this)
        shortAnimTime = resources.getInteger(android.R.integer.config_shortAnimTime)
        actionRefresh()
        presenter.getConsumptionMonth(month, year)

    }

    private fun actionRefresh() {
        swipeRefreshLayoutDays.setOnRefreshListener {
            presenter.getConsumptionMonth(month, year)
        }
    }

    override fun notification(message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }

    override fun initInformations(month: ConsumptionModel, days: List<ConsumptionModel>) {
        if(days.isEmpty()) View.INVISIBLE
        else{
            View.VISIBLE
            recyclerViewDays.apply {
                layoutManager = LinearLayoutManager(activity)
                adapter = DayAdapter(days, month)
            }
        }

    }

    override fun showProgress(show: Boolean) {
        swipeRefreshLayoutDays.isRefreshing = show
        enabledNavigation(show)
        constraintLayoutDay.visibility = if(show) View.INVISIBLE else View.VISIBLE
//        swipeRefreshLayoutDays.visibility = if(show) View.VISIBLE else View.GONE
//
//        swipeRefreshLayoutDays.animate().setDuration(shortAnimTime.toLong()).alpha(if(show) 1F else 0F)
//                .setListener( object : AnimatorListenerAdapter() {
//                    override fun onAnimationEnd(animation: Animator?) {
//                        swipeRefreshLayoutDays.visibility = if(show) View.VISIBLE else View.GONE
//                    }
//                })
    }

    override fun logout() {
        val intentHome = Intent(activity, LoginActivity::class.java)
        Preferences.clearPreferences()
        startActivity(intentHome)
        activity?.finish()
    }


    override fun enabledNavigation(key: Boolean){
        viewHome.enableNavigation(key)
    }






}
