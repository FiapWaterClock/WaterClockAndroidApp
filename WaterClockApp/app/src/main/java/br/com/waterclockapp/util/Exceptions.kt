package br.com.waterclockapp.util

class ValidationException(error:String): Exception(error)

class NotConnectionNetwork:Exception()