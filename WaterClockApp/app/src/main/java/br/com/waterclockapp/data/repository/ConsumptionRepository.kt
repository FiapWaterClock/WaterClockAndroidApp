package br.com.waterclockapp.data.repository

import br.com.waterclockapp.data.ApiConsumption
import br.com.waterclockapp.data.model.ConsumptionModel
import br.com.waterclockapp.domain.Consumption
import br.com.waterclockapp.domain.ConsumptionContract
import br.com.waterclockapp.util.BaseCallback
import br.com.waterclockapp.util.Preferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConsumptionRepository : ConsumptionContract.IRepository {

    override fun getConsumptionMonth(clockId: Int, month: Int, year: Int, onResult: BaseCallback<List<ConsumptionModel>>) {
        ApiConsumption.invoke().getCustomer(clockId, month, year, "bearer ${Preferences.getPreferences()?.token}")
                .enqueue(object : Callback<List<ConsumptionModel>>{
                    override fun onFailure(call: Call<List<ConsumptionModel>>, t: Throwable) {
                        t.message?.let { onResult.onUnsuccessful(it) }
                    }

                    override fun onResponse(call: Call<List<ConsumptionModel>>, response: Response<List<ConsumptionModel>>) {
                        if(response.body() == null || !response.isSuccessful) onResult.onUnsuccessful(response.message())

                        response.body()?.let {
                            onResult.onSuccessful(it)
                        }
                    }

                })
    }

}
