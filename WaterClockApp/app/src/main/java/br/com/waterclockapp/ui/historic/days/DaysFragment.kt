package br.com.waterclockapp.ui.historic.days


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

import br.com.waterclockapp.R
import br.com.waterclockapp.data.model.ConsumptionModel
import br.com.waterclockapp.data.model.DayModel
import br.com.waterclockapp.ui.HomePresenter
import br.com.waterclockapp.ui.historic.HistoricContract
import br.com.waterclockapp.ui.historic.HistoricPresenter
import br.com.waterclockapp.ui.login.LoginActivity
import br.com.waterclockapp.util.Preferences
import kotlinx.android.synthetic.main.fragment_days.*
import java.util.*

class DaysFragment(var month: Int, var year: Int) : Fragment(), HistoricContract.View {


    private lateinit var presenter: HistoricContract.Presenter
    private val days = listOf(
            DayModel(10.2 ,20.34, GregorianCalendar(2019, 10, 10), "Teste de descricao"),
            DayModel(11.2 ,30.34, GregorianCalendar(2019, 10, 11), "Teste de descricao"),
            DayModel(12.2 ,40.34, GregorianCalendar(2019, 10, 12), "Teste de descricao"),
            DayModel(13.2 ,50.34, GregorianCalendar(2019, 10, 13), "Teste de descricao"),
            DayModel(14.2 ,60.0, GregorianCalendar(2019, 10, 14), "Teste de descricao"),
            DayModel(15.2 ,23.34, GregorianCalendar(2019, 10, 15), "Teste de descricao"),
            DayModel(16.2 ,28.34, GregorianCalendar(2019, 10, 16), "Teste de descricao"),
            DayModel(17.2 ,10.34, GregorianCalendar(2019, 10, 17), "Teste de descricao"),
            DayModel(18.2 ,0.44, GregorianCalendar(2019, 10, 18), "Teste de descricao"),
            DayModel(18.2 ,77.44, GregorianCalendar(2019, 10, 19), "Teste de descricao"),
            DayModel(18.2 ,54.44, GregorianCalendar(2019, 10, 20), "Teste de descricao"),
            DayModel(18.2 ,23.44, GregorianCalendar(2019, 10, 21), "Teste de descricao"),
            DayModel(18.2 ,7.44, GregorianCalendar(2019, 10, 22), "Teste de descricao"),
            DayModel(18.2 ,56.44, GregorianCalendar(2019, 10, 23), "Teste de descricao")
    )
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_days, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        presenter = HistoricPresenter(this)
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

    override fun initInformations(models: List<ConsumptionModel>) {
        recyclerViewDays.apply {
            layoutManager = LinearLayoutManager(activity)
            adapter = DayAdapter(days)
        }
    }

    override fun showProgress(show: Boolean) {
        swipeRefreshLayoutDays.isRefreshing = show
    }

    override fun logout() {
        val intentHome = Intent(activity, LoginActivity::class.java)
        Preferences.clearPreferences()
        startActivity(intentHome)
        activity?.finish()
    }






}
