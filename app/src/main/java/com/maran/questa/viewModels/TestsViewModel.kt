package com.maran.questa.viewModels

import android.security.keystore.UserNotAuthenticatedException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.maran.questa.dependencyInjection.PreferencesProvider
import com.maran.questa.network.RetrofitClient
import com.maran.questa.network.apis.AnswerApi
import com.maran.questa.network.apis.QuestionApi
import com.maran.questa.network.apis.ResultApi
import com.maran.questa.network.apis.RoleApi
import com.maran.questa.network.apis.TestApi
import com.maran.questa.network.apis.ThemeApi
import com.maran.questa.network.apis.UserApi
import com.maran.questa.network.models.Model
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import javax.inject.Inject

enum class TestStatus {
    IN_PROCESS,
    SUCCESS,
    FAILURE,
    NOT_LOADED
}

data class TestsState(
    val tests: List<Model.Test> = listOf(),
)

@HiltViewModel
class TestsViewModel @Inject constructor(
    private val preferences: PreferencesProvider
) : ViewModel() {
    private val coroutineScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private lateinit var retrofit: Retrofit
    private lateinit var answerApi: AnswerApi
    private lateinit var questionApi: QuestionApi
    private lateinit var resultApi: ResultApi
    private lateinit var roleApi: RoleApi
    private lateinit var testApi: TestApi
    private lateinit var themeApi: ThemeApi
    private lateinit var userApi: UserApi

    var tests by mutableStateOf<List<Model.Test>>(listOf())

    var testStatus by mutableStateOf(TestStatus.NOT_LOADED)
    private val _uiState = MutableStateFlow(TestsState())

    init {
        retrofit =
            RetrofitClient.retrofitClient(
                preferences.sharedPreferences.getString("token", "")
                    ?: throw UserNotAuthenticatedException()
            )
        answerApi = retrofit.create(AnswerApi::class.java)
        questionApi = retrofit.create(QuestionApi::class.java)
        resultApi = retrofit.create(ResultApi::class.java)
        roleApi = retrofit.create(RoleApi::class.java)
        testApi = retrofit.create(TestApi::class.java)
        themeApi = retrofit.create(ThemeApi::class.java)
        userApi = retrofit.create(UserApi::class.java)

        coroutineScope.launch { tests = getAllTests() }
    }

    suspend fun getAllTests(): List<Model.Test> {
        return coroutineScope.async {
            testStatus = TestStatus.IN_PROCESS
            testApi.getAll().onSuccess { testStatus = TestStatus.SUCCESS }
                .onFailure { testStatus = TestStatus.FAILURE }
        }
            .await().getOrDefault(listOf())
    }

    override fun onCleared() {
        coroutineScope.cancel()
    }
}