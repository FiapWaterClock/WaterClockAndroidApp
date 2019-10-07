package br.com.waterclockapp.ui.historic

import androidx.appcompat.app.AppCompatActivity
import br.com.waterclockapp.data.model.ConsumptionModel

class HistoricContract {

    interface View {
        fun notification(message: String)

        fun initInformations(models: List<ConsumptionModel>)

        fun showProgress(show: Boolean)

        fun logout()
    }

    interface Presenter {

        fun getConsumptionMonth(month:Int, year: Int)

        fun validateNetwork(activity: AppCompatActivity):Boolean
    }
}