package br.com.waterclockapp.data.repository

import br.com.waterclockapp.data.ConsumptionApi
import br.com.waterclockapp.data.model.ConsumptionModel
import br.com.waterclockapp.data.model.RateModel
import br.com.waterclockapp.domain.ConsumptionContract
import br.com.waterclockapp.util.BaseCallback
import br.com.waterclockapp.util.Preferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConsumptionRepository : ConsumptionContract.IRepository {

    override fun getConsumptionMonth(clockId: Int, month: Int, year: Int, onResult: BaseCallback<List<ConsumptionModel>>) {
        ConsumptionApi.invoke().getCustomer(8, month, year, "bearer ${Preferences.getPreferences()?.token}")
                .enqueue(object : Callback<List<ConsumptionModel>>{
                    override fun onFailure(call: Call<List<ConsumptionModel>>, t: Throwable) {
                        t.message?.let { onResult.onUnsuccessful(it) }
                    }

                    override fun onResponse(call: Call<List<ConsumptionModel>>, response: Response<List<ConsumptionModel>>) {
                        if(response.body() == null) return onResult.onUnsuccessful("Lista Vazia")
                        if(!response.isSuccessful) return onResult.onUnsuccessful(response.message())
                        response.body()?.let {
                            onResult.onSuccessful(it)
                        }
                    }

                })
    }

    override fun getConsumptionAllMonth(clockId: Int, month: Int, year: Int, onResult: BaseCallback<ConsumptionModel>) {
        ConsumptionApi.invoke().getCustomerAll(8, month, year, "bearer ${Preferences.getPreferences()?.token}")
                .enqueue(object : Callback<ConsumptionModel>{
                    override fun onFailure(call: Call<ConsumptionModel>, t: Throwable) {
                        t.message?.let { onResult.onUnsuccessful(it) }
                    }

                    override fun onResponse(call: Call<ConsumptionModel>, response: Response<ConsumptionModel>) {
                        if(response.body() == null) return onResult.onUnsuccessful("Lista Vazia")
                        if(!response.isSuccessful) return onResult.onUnsuccessful(response.message())
                        response.body()?.let {
                            onResult.onSuccessful(it)
                        }
                    }

                })
    }

    override fun getConsumptionPriceAllMonth(clockId: Int, month: Int, year: Int, onResult: BaseCallback<RateModel>) {
        ConsumptionApi.invoke().getRateAll(8, month, year, "bearer ${Preferences.getPreferences()?.token}")
                .enqueue(object: Callback<RateModel>{
                    override fun onFailure(call: Call<RateModel>, t: Throwable) {
                        t.message?.let { onResult.onUnsuccessful(it) }

                    }

                    override fun onResponse(call: Call<RateModel>, response: Response<RateModel>) {
                        if(response.body() == null) return onResult.onUnsuccessful("Lista Vazia")
                        if(!response.isSuccessful) return onResult.onUnsuccessful(response.message())
                        response.body()?.let {
                            onResult.onSuccessful(it)
                        }
                    }

                })
    }

}
