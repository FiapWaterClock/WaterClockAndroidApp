package br.com.waterclockapp.domain

import br.com.waterclockapp.util.BaseCallback

class UserContract {

    interface IUser{
        val username: String
        val password: String

        fun isValidEmpty(): Boolean

        fun validationPassword():Boolean

        fun validationCpf():Boolean

        fun validationEmail():Boolean

        fun startLogin(listener: BaseCallback<User>)
    }

    interface IRepository {
        fun startLogin(username: String, password: String, onResult: BaseCallback<User>)
    }
}