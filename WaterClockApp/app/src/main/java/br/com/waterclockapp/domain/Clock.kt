package br.com.waterclockapp.domain

import br.com.waterclockapp.data.model.ClockModel
import br.com.waterclockapp.util.BaseCallback
import br.com.waterclockapp.util.ValidationException

class Clock : ClockContract.IClock {
    var repository: ClockContract.IRepository? = null

    override fun getAllClock(listener: BaseCallback<List<ClockModel>>) {

        if(repository == null) throw ValidationException("Repository nullable")
        repository?.getAllClock(object : BaseCallback<List<ClockModel>>{
            override fun onSuccessful(value: List<ClockModel>) {
                listener.onSuccessful(value)
            }

            override fun onUnsuccessful(error: String) {
                listener.onUnsuccessful(error)
            }

        })

    }
}