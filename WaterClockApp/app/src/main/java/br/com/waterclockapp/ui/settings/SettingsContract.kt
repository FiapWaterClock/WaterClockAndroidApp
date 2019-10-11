package br.com.waterclockapp.ui.settings

import android.content.Context
import br.com.waterclockapp.data.model.ClockModel
import br.com.waterclockapp.data.model.UserModel

class SettingsContract {
    interface View {
        fun notification(message: String)

        fun initRecyclerView(models: List<ClockModel>)

        fun showProgressRecycler(show: Boolean)

        fun logout()

        fun getContext() : Context?
    }

    interface Presenter {

        fun getUserInformation()

        fun updateProfile(userModel: UserModel)

        fun validateNetwork(activity:Context):Boolean

        fun deleteUser()
    }
}