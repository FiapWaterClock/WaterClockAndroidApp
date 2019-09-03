package br.com.waterclockapp.domain

import android.os.Parcel
import android.os.Parcelable
import br.com.waterclockapp.util.*
import java.util.regex.Pattern

class User(override var username: String, override var password: String) : UserContract.IUser, Parcelable {


    var repository: UserContract.IRepository? = null

    var name: String? = null
    var userId: Int? = null
    var token: String? = null

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString()) {
        username = parcel.readString()
        password = parcel.readString()
        name = parcel.readString()
        userId = parcel.readValue(Int::class.java.classLoader) as? Int
        token = parcel.readString()
    }

    constructor(
            username: String,
            password: String,
            name: String,
            userId: Int,
            token: String
    ):this(username, password) {
        this.name = name
        this.userId = userId
        this.token = token
    }

    override fun isValidEmpty(): Boolean = (username.isEmpty() || password.isEmpty())

    override fun validationPassword():Boolean{
        val pattern = Pattern.compile(PASSWORD_PATTERN)
        val matcher = pattern.matcher(password)
        return matcher.matches()
    }

    override fun validationCpf():Boolean {
        val pattern = Pattern.compile(CPF_PATTERN)
        val matcher = pattern.matcher(replaceChars(username))
        return matcher.matches()
    }

    private fun replaceChars(cpfFull : String) : String {
        return cpfFull.replace(".", "").replace("-", "")
                .replace("(", "").replace(")", "")
                .replace("/", "").replace(" ", "")
                .replace("*", "")
    }

    override fun validationEmail():Boolean = Formation.emailFormat(username)

    override fun startLogin(listener: BaseCallback<User>) {

        if(repository == null) throw ValidationException("Repository nullable")

        if(isValidEmpty()) throw ValidationException("User or Password is empty")

        if(!(validationCpf() || validationEmail()))
            throw ValidationException("User field must be a email or CPF format")

        if(!validationPassword()) throw ValidationException("Password format is incorrect")

        repository?.startLogin(username, password, object : BaseCallback<User>{
            override fun onSuccessful(value: User) {
                listener.onSuccessful(value)
            }

            override fun onUnsuccessful(error: String) {
                listener.onUnsuccessful(error)
            }
        })
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(username)
        parcel.writeString(password)
        parcel.writeString(name)
        parcel.writeValue(userId)
        parcel.writeString(token)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<User> {
        override fun createFromParcel(parcel: Parcel): User {
            return User(parcel)
        }

        override fun newArray(size: Int): Array<User?> {
            return arrayOfNulls(size)
        }
    }
}