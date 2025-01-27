package com.maran.questa.viewModels

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.maran.questa.dependencyInjection.PreferencesProvider
import com.maran.questa.network.RetrofitClient
import com.maran.questa.network.apis.RoleApi
import com.maran.questa.network.apis.SecurityApi
import com.maran.questa.network.apis.UserApi
import com.maran.questa.network.models.Model
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import java.util.UUID
import javax.inject.Inject

enum class VerificationError {
    EMPTY,
    DIFFERENT,
    CORRECT
}

enum class LoginStatus {
    NOT_LOGGED,
    IN_PROCESS,
    SUCCESS,
    FAILURE
}

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val preferences: PreferencesProvider
) : ViewModel() {
    private val coroutineScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private lateinit var retrofit: Retrofit
    private lateinit var securityService: SecurityApi
    private lateinit var userService: UserApi
    private lateinit var roleService: RoleApi

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

    var passwordVerification by mutableStateOf("")
        private set

    fun updatePasswordVerification(input: String) {
        passwordVerification = input
    }

    var loginStatus by mutableStateOf(LoginStatus.IN_PROCESS)
    var loginMessage by mutableStateOf("")

    var isErrorUsername by mutableStateOf(false)

    var isErrorPassword by mutableStateOf(false)

    var isErrorPasswordVerification by mutableStateOf(false)

    fun validateUsername(text: CharSequence) {
        isErrorUsername = text.isEmpty()
    }

    fun validatePassword(text: CharSequence) {
        isErrorPassword = text.isEmpty()
    }

    fun validatePasswordSignUp(passwordText: CharSequence, passwordVerification: CharSequence) {
        isErrorPasswordVerification =
            passwordText.isEmpty() || passwordVerification.isEmpty() || passwordVerification != passwordText
    }

    fun getVerificationError(): VerificationError {
        if (passwordVerification.isEmpty()) {
            return VerificationError.EMPTY
        } else if (passwordVerification != password) {
            return VerificationError.DIFFERENT
        }

        return VerificationError.CORRECT
    }

    init {
        val retrofitAuth = RetrofitClient.retrofitClientAuthentication()
        securityService = retrofitAuth.create(SecurityApi::class.java)
        retrofit =
            RetrofitClient.retrofitClient(preferences.sharedPreferences.getString("token", "")!!)
        userService = retrofit.create(UserApi::class.java)
        roleService = retrofit.create(RoleApi::class.java)
        check()
    }

    fun login() {
        coroutineScope.launch {
            loginStatus = LoginStatus.IN_PROCESS
            securityService.login(Model.Authentication(username, password))
                .onSuccess { token ->
                    with(preferences.sharedPreferences.edit()) {
                        putString("token", token)
                        apply()
                    }
                    loginStatus = LoginStatus.SUCCESS
                }
                .onFailure {
                    loginStatus = LoginStatus.FAILURE
                    loginMessage = it.localizedMessage
                }
        }
    }

    fun signUp() {
        coroutineScope.launch {
            loginStatus = LoginStatus.IN_PROCESS
            val role = roleService.getByName("basic").onSuccess { it ->
                userService.insert(Model.User(UUID.randomUUID(), username, it[0], password))
                    .onSuccess {
                        loginStatus = LoginStatus.SUCCESS
                    }
                    .onFailure {
                        loginStatus = LoginStatus.FAILURE
                        loginMessage = it.localizedMessage
                    }
            }
        }
    }

    fun check() {
        coroutineScope.launch {
            loginStatus = LoginStatus.IN_PROCESS
            userService.check()
                .onSuccess {
                    loginStatus = LoginStatus.SUCCESS
                }
                .onFailure {
                    loginStatus = LoginStatus.NOT_LOGGED
                }
        }
    }

    override fun onCleared() {
        coroutineScope.cancel()
    }
}