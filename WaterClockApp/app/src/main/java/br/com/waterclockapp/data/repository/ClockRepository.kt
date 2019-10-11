package br.com.waterclockapp.data.repository

import br.com.waterclockapp.data.ClockApi
import br.com.waterclockapp.data.model.ClockModel
import br.com.waterclockapp.domain.ClockContract
import br.com.waterclockapp.util.BaseCallback
import br.com.waterclockapp.util.Preferences
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ClockRepository : ClockContract.IRepository{
    override fun getAllClock(onResult: BaseCallback<List<ClockModel>>) {
        Preferences.getPreferences()?.token?.let {
            ClockApi.invoke().getAllClocks("bearer $it").enqueue(object : Callback<List<ClockModel>>{
                override fun onFailure(call: Call<List<ClockModel>>, t: Throwable) {
                    t.message?.let { message -> onResult.onUnsuccessful(message) }
                }

                override fun onResponse(call: Call<List<ClockModel>>, response: Response<List<ClockModel>>) {
                    if(!response.isSuccessful) return onResult.onUnsuccessful(response.message())
                    if(response.body() == null) return onResult.onUnsuccessful("Lista Vazia")
                    response.body()?.let { list ->
                        onResult.onSuccessful(list)
                    }
                }
            })

        }
    }


}