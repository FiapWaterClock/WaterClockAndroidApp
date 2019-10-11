package br.com.waterclockapp.ui.register

import android.content.Context
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import br.com.waterclockapp.data.model.RegisterModel
import br.com.waterclockapp.data.model.UserModel
import br.com.waterclockapp.data.repository.UserRepository
import br.com.waterclockapp.domain.User
import br.com.waterclockapp.util.BaseCallback
import br.com.waterclockapp.util.CONNECTION_INTERNTET_ERROR
import br.com.waterclockapp.util.NotConnectionNetwork
import br.com.waterclockapp.util.ValidationException

class RegisterPresenter(val view: RegisterContract.View): RegisterContract.Presenter {
    override fun createNewUser(register: RegisterModel) {
        try {
            if (validateNetwork(view.getActivity())) return
            val user: User =
                    User(register.email, register.password)
            user.repository = UserRepository()
            view.showProgressBar(true)
            user.createNewUser(register, object : BaseCallback<UserModel> {
                override fun onSuccessful(value: UserModel) {
                    view.notification("Cadastrado")
                    view.goToHome(user)
                }

                override fun onUnsuccessful(error: String) {
                    view.notification(error)
                }

            })
        }catch (validate: ValidationException){
            view.notification(validate.message ?: "Erro encontrado")
        }catch (e: Exception){
            view.notification(e.message ?: "Erro encontrado")
        }

    }


    override fun validateNetwork(activity: AppCompatActivity): Boolean {
        return try {
            verifyNetwork(activity)
            false
        }catch (e: NotConnectionNetwork) {
            view.notification(CONNECTION_INTERNTET_ERROR)
            view.logout()
            true
        }
    }

    private fun verifyNetwork(activity:AppCompatActivity){
        val connectivityManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        if (networkInfo == null || !networkInfo.isConnected) throw NotConnectionNetwork()

    }
}