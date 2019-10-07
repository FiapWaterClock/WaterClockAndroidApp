package br.com.waterclockapp.domain

import br.com.waterclockapp.data.model.ConsumptionModel
import br.com.waterclockapp.util.BaseCallback
import br.com.waterclockapp.util.ValidationException

class Consumption(override var month: Int, override var year: Int): ConsumptionContract.IConsumption {

    var repository: ConsumptionContract.IRepository? = null


    override fun getConsumptionMonth(listener: BaseCallback<List<ConsumptionModel>>) {
        if(isValidEmpty()) throw ValidationException("Month or Year is empty")

        repository?.getConsumptionMonth(123, month, year, object: BaseCallback<List<ConsumptionModel>>{
            override fun onSuccessful(value: List<ConsumptionModel>) {
                listener.onSuccessful(value)
            }

            override fun onUnsuccessful(error: String) {
                listener.onUnsuccessful(error)
            }

        })
    }

    override fun isValidEmpty(): Boolean = (month == 0 || year == 0)
}