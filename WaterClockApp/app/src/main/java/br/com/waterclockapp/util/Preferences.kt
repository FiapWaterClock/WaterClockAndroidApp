package br.com.waterclockapp.util

import android.content.Context
import android.content.SharedPreferences
import br.com.waterclockapp.domain.User

object Preferences {

    private var shared: SharedPreferences? = null

    operator fun invoke(context: Context): Preferences {
        val sharedPreference: SharedPreferences by lazy {
            context.getSharedPreferences(NAME_PREFS, Context.MODE_PRIVATE)
        }
        shared = sharedPreference
        return this
    }

    private var user: User? = null

    fun saveUser(user: User) {
        this.user = user

        shared?.let {
            it.edit()
                .putString(USERNAME, user.username)
                .putString(PASSWORD, user.password)
                .putString(NAME, user.name)
                .putInt(USERID, user.userId!!)
                .putString(TOKEN, user.token)
                .apply()
        }

        savedPreferences()
    }



    @Suppress("NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
    fun savedPreferences() {
        user?.let { user ->
            shared?.let{
                user.username = it.getString(USERNAME, "")
                user.password = it.getString(PASSWORD, "")
                user.token = it.getString(TOKEN, "")
            }

        }
    }

    fun getPreferences(): User? = if(isSave()) user else null


    fun isSave():Boolean {
        savedPreferences()
        return user != null
    }

    fun clearPreferences() {
        shared?.let {
            val editor: SharedPreferences.Editor = it.edit()
            editor.clear()
            editor.apply()
        }
    }


}