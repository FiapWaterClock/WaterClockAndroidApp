package br.com.waterclockapp.domain

import br.com.waterclockapp.data.model.ConsumptionModel
import br.com.waterclockapp.data.model.RateModel
import br.com.waterclockapp.util.BaseCallback

class ConsumptionContract {

    interface IConsumption{
        val month: Int
        val year: Int

        fun isValidEmpty(): Boolean
        fun getConsumptionMonth(listener: BaseCallback<List<ConsumptionModel>>)

        fun getConsumptionAllMonth(listener: BaseCallback<ConsumptionModel>)

        fun getConsumptionPriceAllMonth(listener: BaseCallback<RateModel>)
    }

    interface IRepository{
         fun getConsumptionMonth(clockId: Int, month:Int, year:Int,  onResult: BaseCallback<List<ConsumptionModel>>)

        fun getConsumptionAllMonth(clockId: Int, month:Int, year:Int,  onResult: BaseCallback<ConsumptionModel>)

        fun getConsumptionPriceAllMonth(clockId: Int, month:Int, year:Int,  onResult: BaseCallback<RateModel>)
    }
}