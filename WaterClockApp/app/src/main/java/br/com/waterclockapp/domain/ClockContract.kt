package br.com.waterclockapp.domain

import br.com.waterclockapp.data.model.ClockModel
import br.com.waterclockapp.util.BaseCallback

class ClockContract {

    interface IClock{

        fun getAllClock(listener: BaseCallback<List<ClockModel>>)
    }

    interface IRepository{
        fun getAllClock(onResult: BaseCallback<List<ClockModel>>)
    }
}