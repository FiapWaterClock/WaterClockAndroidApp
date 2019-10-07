package br.com.waterclockapp.domain

import br.com.waterclockapp.data.model.ConsumptionModel
import br.com.waterclockapp.util.BaseCallback

class ConsumptionContract {

    interface IConsumption{
        val month: Int
        val year: Int

        fun isValidEmpty(): Boolean
        fun getConsumptionMonth(listener: BaseCallback<List<ConsumptionModel>>)
    }

    interface IRepository{
         fun getConsumptionMonth(clockId: Int, month:Int, year:Int,  onResult: BaseCallback<List<ConsumptionModel>>)
    }
}