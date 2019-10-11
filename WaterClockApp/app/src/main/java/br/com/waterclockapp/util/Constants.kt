package br.com.waterclockapp.util

const val BASE_URL = "https://bank-app-test.herokuapp.com/api/"
const val PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=])(?=\\S+$).{4,}$"
const val CPF_PATTERN = "^([0-9]{3}\\.?){3}-?[0-9]{2}$"
const val EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"

const val USERNAME = "username"
const val PASSWORD = "password"
const val TOKEN = "token"
const val REMEMBER = "remember"
const val NAME_PREFS = "user_preferences"
const val USERID = "user_id"
const val NAME = "firstName"

const val USER_ACCOUNT:String ="USER_ACCOUNT"

const val DIALOG_MESSAGE_CLEAN_SETTINGS = "Deseja realmente limpar as credenciais salvas?";

const val DIALOG_SEND_IMAGE = "Deseja realmente enviar essa imagem?"

const val CONNECTION_INTERNTET_ERROR = "Internet sem conexão"

const val CLEAR_SUCCESS_PREFERENCES = "Preferências limpas"

const val DELETE_USER = "Deseja realmente deletar esse usuário?"

const val DELETE_SUCCESS_USER = "Usuario deletado com sucesso"