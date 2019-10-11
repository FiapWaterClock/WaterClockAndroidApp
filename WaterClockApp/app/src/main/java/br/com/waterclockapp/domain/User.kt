package br.com.waterclockapp.domain

import android.os.Parcel
import android.os.Parcelable
import br.com.waterclockapp.data.model.RegisterModel
import br.com.waterclockapp.data.model.UserModel
import br.com.waterclockapp.util.*
import java.util.regex.Pattern

class User() : UserContract.IUser, Parcelable {
    override var username: String? = null
    override var password: String? = null

    var repository: UserContract.IRepository? = null
    var email: String? = null
    var userId: Int? = null
    var token: String? = null
    var name: String? = null
    var clockId: Int? = null


    constructor(username: String, password: String):this(){
        this.username = username
        this.password = password
    }

    constructor(parcel: Parcel) : this(
            parcel.readString(),
            parcel.readString(),
            parcel.readString(),
            parcel.readInt(),
            parcel.readString()) {
        username = parcel.readString()
        password = parcel.readString()
        email = parcel.readString()
        userId = parcel.readValue(Int::class.java.classLoader) as? Int
        token = parcel.readString()
    }

    constructor(
            username: String,
            password: String,
            email: String,
            userId: Int,
            token: String
    ):this(username, password) {
        this.email = email
        this.userId = userId
        this.token = token
    }

    constructor(
            userId: Int,
            name: String,
            clockId: Int
    ):this(){
        this.userId = userId
        this.name = name
        this.clockId = clockId
    }

    override fun isValidEmpty(): Boolean = ((username ?: "").isEmpty() || (password ?: "").isEmpty())

    override fun validationPassword():Boolean{
        val pattern = Pattern.compile(PASSWORD_PATTERN)
        val matcher = pattern.matcher(password)
        return matcher.matches()
    }

    override fun validationCpf():Boolean {
        val pattern = Pattern.compile(CPF_PATTERN)
        val matcher = pattern.matcher(replaceChars(username ?: "") )
        return matcher.matches()
    }

    private fun replaceChars(cpfFull : String) : String {
        return cpfFull.replace(".", "").replace("-", "")
                .replace("(", "").replace(")", "")
                .replace("/", "").replace(" ", "")
                .replace("*", "")
    }

    override fun validationEmail():Boolean = Formation.emailFormat(username ?: "")

    override fun startLogin(listener: BaseCallback<User>) {

        if(repository == null) throw ValidationException("Repository nullable")

        if(isValidEmpty()) throw ValidationException("User or Password is empty")

        if(!validationEmail())
            throw ValidationException("User field must be a email or CPF format")

        //if(!validationPassword()) throw ValidationException("Password format is incorrect")

        repository?.startLogin(username ?: "", password ?: "", object : BaseCallback<User>{
            override fun onSuccessful(value: User) {
                listener.onSuccessful(value)
            }

            override fun onUnsuccessful(error: String) {
                listener.onUnsuccessful(error)
            }
        })
    }

    override fun getUserInformation(listener: BaseCallback<UserModel>) {
        if(repository == null) throw ValidationException("Repository nullable")
        //if(email == null) throw ValidationException("User not found")
        if(token == null) throw ValidationException("Token not found")
        repository?.getUserInformation(email ?: "", token?: "", object : BaseCallback<UserModel>{
            override fun onSuccessful(value: UserModel) {
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
        parcel.writeString(email)
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

    override fun createNewUser(register: RegisterModel, listener: BaseCallback<UserModel>) {
        if(repository == null) throw ValidationException("Repository nullable")
        if(register.email.isEmpty() || register.firstName.isEmpty() || register.lastName.isEmpty()
                || register.password.isEmpty() || register.matchingPassword.isEmpty())
            throw ValidationException("Fill in all values")

        repository?.createNewUser(register, object : BaseCallback<UserModel>{
            override fun onSuccessful(value: UserModel) {
                listener.onSuccessful(value)
            }

            override fun onUnsuccessful(error: String) {
                listener.onUnsuccessful(error)
            }

        })
    }

    override fun deleteUser(listener: BaseCallback<Void>) {
        if(repository == null) throw ValidationException("Repository nullable")
        userId = 1
        if(userId == 0) throw ValidationException("Repository nullable")
        repository?.deleteUser(userId!!, object : BaseCallback<Void>{
            override fun onSuccessful(value: Void) {
                listener.onSuccessful(value)
            }

            override fun onUnsuccessful(error: String) {
                listener.onUnsuccessful(error)
            }

        })
    }
}