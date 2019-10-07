package br.com.waterclockapp.ui.information

import androidx.appcompat.app.AppCompatActivity
import br.com.waterclockapp.data.model.ConsumptionModel

class InformationContract{
    interface View {
        fun notification(message: String)

        fun initRecyclerView(models: List<ConsumptionModel>)

        fun showProgressRecycler(show: Boolean)

        fun getActivity(): AppCompatActivity
    }

    interface Presenter {

        fun getConsumptionMonth(month:Int, year: Int)

        fun validateNetwork(activity: AppCompatActivity):Boolean
    }
}