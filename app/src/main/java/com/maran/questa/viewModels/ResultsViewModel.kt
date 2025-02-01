package com.maran.questa.viewModels

import android.security.keystore.UserNotAuthenticatedException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.maran.questa.dependencyInjection.PreferencesProvider
import com.maran.questa.network.RetrofitClient
import com.maran.questa.network.apis.ResultApi
import com.maran.questa.network.models.Model
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import java.util.UUID
import javax.inject.Inject

enum class ResultState {
    IN_PROCESS,
    SUCCESS,
    FAILURE,
    NOT_FOUND
}

@HiltViewModel
class ResultsViewModel @Inject constructor(
    private val preferences: PreferencesProvider
) : ViewModel() {
    private val coroutineScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private var retrofit: Retrofit =
        RetrofitClient.retrofitClient(
            preferences.sharedPreferences.getString("token", "")
                ?: throw UserNotAuthenticatedException()
        )
    private val resultApi: ResultApi = retrofit.create(ResultApi::class.java)

    var resultState by mutableStateOf(ResultState.IN_PROCESS)

    var resultDescription by mutableStateOf("")

    fun getResult(testId: UUID, isPersonality: Boolean, personality: String?, score: Int?) {
        resultState = ResultState.IN_PROCESS
        coroutineScope.launch {
            resultApi.getByTest(testId).onSuccess {
                val result: Model.Result? = if (isPersonality) {
                    it.find { res -> res.personality == personality }
                } else {
                    it.sortedBy { res -> res.maxPoints }.last { res -> res.maxPoints!! < score!! }

                }
                if (result == null) {
                    resultState = ResultState.NOT_FOUND
                } else {
                    resultDescription = result.resultMessage
                    resultState = ResultState.SUCCESS
                }
            }.onFailure {
                resultState = ResultState.FAILURE
            }
        }
    }
}