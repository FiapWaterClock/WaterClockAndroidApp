package br.com.waterclockapp.util

import android.content.Context
import android.content.SharedPreferences
import br.com.waterclockapp.domain.User

object Preferences {

    private var shared: SharedPreferences? = null
    private var remember: Rebember? = null

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
                .putString(EMAIL, user.email)
                .putInt(USERID, user.userId ?: 36)
                .putString(TOKEN, user.token)
                    .putString(NAME, user.name)
                    .putInt(CLOCK, user.clockId?: 8)
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

    fun saveSwitch(remember: Rebember){
        this.remember = remember
        shared?.let {
            it.edit().putInt(REMEMBER, remember.ordinal).apply()
        }
        savedPreferences()
    }

    fun getPreferences(): User? = if(isSave()) user else null

    fun getPreferencesRemember(): Rebember? = remember


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