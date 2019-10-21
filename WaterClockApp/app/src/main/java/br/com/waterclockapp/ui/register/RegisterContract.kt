package br.com.waterclockapp.ui.register

import androidx.appcompat.app.AppCompatActivity
import br.com.waterclockapp.data.model.RegisterModel
import br.com.waterclockapp.domain.User

class RegisterContract {

    interface View {
        fun notification(message: String)

        fun goToHome(user: User)

        fun showProgressBar(show: Boolean)

        fun getActivity() : AppCompatActivity

        fun logout()
    }

    interface Presenter {

        fun createNewUser(register: RegisterModel)

        fun validateNetwork(activity: AppCompatActivity):Boolean
    }
}