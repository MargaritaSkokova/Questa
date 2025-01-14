package com.maran.questa

import android.security.keystore.UserNotAuthenticatedException
import androidx.lifecycle.ViewModel
import com.maran.questa.network.RetrofitClient
import com.maran.questa.network.apis.AnswerApi
import com.maran.questa.network.apis.QuestionApi
import com.maran.questa.network.apis.ResultApi
import com.maran.questa.network.apis.RoleApi
import com.maran.questa.network.apis.SecurityApi
import com.maran.questa.network.apis.TestApi
import com.maran.questa.network.apis.ThemeApi
import com.maran.questa.network.apis.UserApi
import com.maran.questa.network.models.Model
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import javax.inject.Inject

@HiltViewModel
class TestsViewModel @Inject constructor(
    private val preferences: PreferencesProvider
) : ViewModel() {
    private val coroutineScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    lateinit var job: Job
    private lateinit var retrofit: Retrofit
    private lateinit var answerApi: AnswerApi
    private lateinit var questionApi: QuestionApi
    private lateinit var resultApi: ResultApi
    private lateinit var roleApi: RoleApi
    private lateinit var testApi: TestApi
    private lateinit var themeApi: ThemeApi
    private lateinit var userApi: UserApi

    init {
        val retrofitAuth = RetrofitClient.retrofitClientAuthentication()
        val securityService = retrofitAuth.create(SecurityApi::class.java)

        job = coroutineScope.launch {
            val token = securityService.login(Model.Authentication("xxx", "xxx"))
            with(preferences.sharedPreferences.edit()) {
                putString("token", token)
                apply()
            }

            retrofit =
                RetrofitClient.retrofitClient(
                    preferences.sharedPreferences.getString("token", "") ?: throw UserNotAuthenticatedException()
                )
            answerApi = retrofit.create(AnswerApi::class.java)
            questionApi = retrofit.create(QuestionApi::class.java)
            resultApi = retrofit.create(ResultApi::class.java)
            roleApi = retrofit.create(RoleApi::class.java)
            testApi = retrofit.create(TestApi::class.java)
            themeApi = retrofit.create(ThemeApi::class.java)
            userApi = retrofit.create(UserApi::class.java)
        }
    }

    suspend fun getAllTests(): List<Model.Test> {
        job.join()
        return coroutineScope.async { testApi.getAll() }.await()
    }
}