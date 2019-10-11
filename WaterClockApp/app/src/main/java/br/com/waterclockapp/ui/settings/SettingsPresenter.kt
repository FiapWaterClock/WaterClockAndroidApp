package br.com.waterclockapp.ui.settings

import android.content.Context
import android.net.ConnectivityManager
import br.com.waterclockapp.data.model.UserModel
import br.com.waterclockapp.data.repository.UserRepository
import br.com.waterclockapp.domain.User
import br.com.waterclockapp.util.BaseCallback
import br.com.waterclockapp.util.CONNECTION_INTERNTET_ERROR
import br.com.waterclockapp.util.NotConnectionNetwork
import br.com.waterclockapp.util.Preferences

class SettingsPresenter(val view: SettingsContract.View) : SettingsContract.Presenter {
    override fun getUserInformation() {
        view.getContext()?.let { verifyNetwork(it) }

        Preferences.getPreferences()?.let {
            val user: User = it
            user.repository = UserRepository()
            user.getUserInformation(object : BaseCallback<UserModel>{
                override fun onSuccessful(value: UserModel) {
                    view.initRecyclerView(value.clock)
                }

                override fun onUnsuccessful(error: String) {
                    view.notification(error)
                }

            })
        }


    }

    override fun validateNetwork(activity: Context): Boolean {
        return try {
            verifyNetwork(activity)
            false
        }catch (e: NotConnectionNetwork) {
            view.notification(CONNECTION_INTERNTET_ERROR)
            view.logout()
            true
        }
    }

    private fun verifyNetwork(activity: Context){
        val connectivityManager = activity.getSystemService(Context.CONNECTIVITY_SERVICE)
                as ConnectivityManager
        val networkInfo = connectivityManager.activeNetworkInfo
        if (networkInfo == null || !networkInfo.isConnected) throw NotConnectionNetwork()

    }

    override fun deleteUser() {
        view.getContext()?.let { verifyNetwork(it) }

        Preferences.getPreferences()?.let {
            val user: User = it
            user.repository = UserRepository()
            user.deleteUser(object : BaseCallback<Void>{
                override fun onSuccessful(value: Void) {
                    view.notification("Usuário Deletado")
                    view.logout()
                }

                override fun onUnsuccessful(error: String) {
                    view.notification(error)
                }

            })
        }
    }
}