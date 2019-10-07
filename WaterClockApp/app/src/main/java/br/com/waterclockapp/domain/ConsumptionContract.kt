package br.com.waterclockapp.domain

import br.com.waterclockapp.util.BaseCallback

class ConsumptionContract {

    interface IConsumption{

    }

    interface IRepository{
        fun getConsumptionMonth(clockId: Int, month:Int, year:Int,  onResult: BaseCallback<List<Consumption>>)
    }
}