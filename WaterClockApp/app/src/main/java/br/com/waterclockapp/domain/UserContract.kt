package br.com.waterclockapp.domain

import br.com.waterclockapp.data.model.RegisterModel
import br.com.waterclockapp.data.model.UserModel
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

        fun getUserInformation(listener: BaseCallback<UserModel>)

        fun createNewUser(register: RegisterModel, listener: BaseCallback<UserModel>)

        fun deleteUser(listener: BaseCallback<Void>)
    }

    interface IRepository {
        fun startLogin(username: String, password: String, onResult: BaseCallback<User>)

        fun getUserInformation(onResult: BaseCallback<UserModel>)

        fun createNewUser(register: RegisterModel, onResult: BaseCallback<UserModel>)

        fun deleteUser(userId: Int, onResult: BaseCallback<Void>)
    }
}