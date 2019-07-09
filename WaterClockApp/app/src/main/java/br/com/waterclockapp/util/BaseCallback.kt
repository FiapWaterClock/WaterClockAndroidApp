package br.com.waterclockapp.util


interface BaseCallback<T> {
    fun onSuccessful(value : T)
    fun onUnsuccessful(error: String)
}