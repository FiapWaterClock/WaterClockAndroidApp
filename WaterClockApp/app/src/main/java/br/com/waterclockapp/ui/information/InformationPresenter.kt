package br.com.waterclockapp.ui.information

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import br.com.waterclockapp.data.model.ConsumptionModel
import br.com.waterclockapp.data.repository.ConsumptionRepository
import br.com.waterclockapp.domain.Consumption
import br.com.waterclockapp.util.BaseCallback
import br.com.waterclockapp.util.CONNECTION_INTERNTET_ERROR
import br.com.waterclockapp.util.NotConnectionNetwork

class InformationPresenter(val view: InformationContract.View) : InformationContract.Presenter {

    override fun getConsumptionMonth(month: Int, year: Int) {

        val consumption = Consumption(month, year)
        consumption.repository = ConsumptionRepository()
        try{
            consumption.getConsumptionAllMonth(object : BaseCallback<ConsumptionModel> {
                override fun onSuccessful(value: ConsumptionModel) {
                    getConsumptionDay(consumption, value)
                }

                override fun onUnsuccessful(error: String) {
                    view.notification(error)
                }

            })
        }catch (e: Exception){
            e.message?.let { view.notification(it) }
        }
    }

    private fun getConsumptionDay(consumption: Consumption, month: ConsumptionModel) {
        consumption.getConsumptionMonth(object : BaseCallback<List<ConsumptionModel>>{
            override fun onSuccessful(value: List<ConsumptionModel>) {
                if(value.isEmpty()) return view.initView(month, month)
                view.initView(month, value[value.size - 1])
            }

            override fun onUnsuccessful(error: String) {
                view.notification(error)
            }

        })
    }

    override fun validateNetwork(activity: AppCompatActivity): Boolean {
        return try {
            verifyNetwork(activity)
            false
        }catch (e: NotConnectionNetwork) {
            view.notification(CONNECTION_INTERNTET_ERROR)
            view.logout()
            true
        }
    }

    private fun verifyNetwork(activity:AppCompatActivity){
        val connectivityManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        if (networkInfo == null || !networkInfo.isConnected) throw NotConnectionNetwork()

    }
}