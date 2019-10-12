package br.com.waterclockapp.ui.information

import androidx.appcompat.app.AppCompatActivity
import br.com.waterclockapp.data.model.ConsumptionModel

class InformationContract{
    interface View {
        fun notification(message: String)

        fun initView(consumptionMonth: ConsumptionModel, consumptionDay: ConsumptionModel)

        fun showProgressRecycler(show: Boolean)

        fun logout()

        fun enabledNavigation(key: Boolean)
    }

    interface Presenter {

        fun getConsumptionMonth(month:Int, year: Int)

        fun validateNetwork(activity: AppCompatActivity):Boolean
    }
}