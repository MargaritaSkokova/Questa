package com.maran.questa

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.maran.questa.network.RetrofitClient
import com.maran.questa.network.apis.SecurityApi
import com.maran.questa.network.models.Model
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val preferences: PreferencesProvider
) : ViewModel() {
    private val coroutineScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private lateinit var retrofit: Retrofit
    private lateinit var securityService: SecurityApi

    var username by mutableStateOf("")
        private set

    fun updateUsername(input: String) {
        username = input
    }

    var password by mutableStateOf("")
        private set

    fun updatePassword(input: String) {
        password = input
    }

    var loginStatus by mutableStateOf("not_logged")
    var loginMessage by mutableStateOf("")

    var isErrorUsername by mutableStateOf(false)

    var isErrorPassword by mutableStateOf(false)

    fun validateUsername(text: CharSequence) {
        isErrorUsername = text.isEmpty()
    }

    fun validatePassword(text: CharSequence) {
        isErrorPassword = text.isEmpty()
    }

    init {
        val retrofitAuth = RetrofitClient.retrofitClientAuthentication()
        securityService = retrofitAuth.create(SecurityApi::class.java)
    }

    fun login() {
        coroutineScope.launch {
            loginStatus = "in_process"
            securityService.login(Model.Authentication(username, password))
                .onSuccess { token ->
                    with(preferences.sharedPreferences.edit()) {
                        putString("token", token)
                        apply()
                    }
                    loginStatus = "success"
                }
                .onFailure {
                    loginStatus = "failure"
                    loginMessage = it.localizedMessage
                }
        }
    }

    override fun onCleared() {
        coroutineScope.cancel()
    }
}