package br.com.waterclockapp.data.repository

import br.com.waterclockapp.data.ConsumptionApi
import br.com.waterclockapp.data.model.ConsumptionModel
import br.com.waterclockapp.domain.ConsumptionContract
import br.com.waterclockapp.util.BaseCallback
import br.com.waterclockapp.util.Preferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConsumptionRepository : ConsumptionContract.IRepository {

    override fun getConsumptionMonth(clockId: Int, month: Int, year: Int, onResult: BaseCallback<List<ConsumptionModel>>) {
        ConsumptionApi.invoke().getCustomer(clockId, month, year, "bearer ${Preferences.getPreferences()?.token}")
                .enqueue(object : Callback<List<ConsumptionModel>>{
                    override fun onFailure(call: Call<List<ConsumptionModel>>, t: Throwable) {
                        t.message?.let { onResult.onUnsuccessful(it) }
                    }

                    override fun onResponse(call: Call<List<ConsumptionModel>>, response: Response<List<ConsumptionModel>>) {
                        if(!response.isSuccessful) return onResult.onUnsuccessful(response.message())
                        if(response.body() == null) return onResult.onUnsuccessful("Lista Vazia")
                        response.body()?.let {
                            onResult.onSuccessful(it)
                        }
                    }

                })
    }

    override fun getConsumptionAllMonth(clockId: Int, month: Int, year: Int, onResult: BaseCallback<ConsumptionModel>) {
        ConsumptionApi.invoke().getCustomerAll(clockId, month, year, "bearer ${Preferences.getPreferences()?.token}")
                .enqueue(object : Callback<ConsumptionModel>{
                    override fun onFailure(call: Call<ConsumptionModel>, t: Throwable) {
                        t.message?.let { onResult.onUnsuccessful(it) }
                    }

                    override fun onResponse(call: Call<ConsumptionModel>, response: Response<ConsumptionModel>) {
                        if(!response.isSuccessful) return onResult.onUnsuccessful(response.message())
                        if(response.body() == null) return onResult.onUnsuccessful("Lista Vazia")
                        response.body()?.let {
                            onResult.onSuccessful(it)
                        }
                    }

                })
    }

}
