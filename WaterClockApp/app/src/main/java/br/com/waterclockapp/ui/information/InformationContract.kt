package br.com.waterclockapp.ui.information

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import br.com.waterclockapp.data.model.ConsumptionModel

class InformationContract{
    interface View {
        fun notification(message: String)

        fun initRecyclerView(models: List<ConsumptionModel>)

        fun showProgressRecycler(show: Boolean)


        fun logout()
    }

    interface Presenter {

        fun getConsumptionMonth(month:Int, year: Int)

        fun validateNetwork(activity: AppCompatActivity):Boolean
    }
}