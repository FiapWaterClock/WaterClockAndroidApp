package br.com.waterclockapp.data.repository

import br.com.waterclockapp.domain.Consumption
import br.com.waterclockapp.domain.ConsumptionContract
import br.com.waterclockapp.domain.UserContract
import br.com.waterclockapp.util.BaseCallback

class ConsumptionRepository : ConsumptionContract.IRepository{

    override fun getConsumptionMonth(clockId: Int, month: Int, year: Int, onResult: BaseCallback<List<Consumption>>) {

    }

}
