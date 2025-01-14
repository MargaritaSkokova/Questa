package com.maran.questa

import android.security.keystore.UserNotAuthenticatedException
import androidx.lifecycle.ViewModel
import com.maran.questa.network.RetrofitClient
import com.maran.questa.network.apis.AnswerApi
import com.maran.questa.network.apis.QuestionApi
import com.maran.questa.network.apis.ResultApi
import com.maran.questa.network.apis.RoleApi
import com.maran.questa.network.apis.TestApi
import com.maran.questa.network.apis.ThemeApi
import com.maran.questa.network.apis.UserApi
import com.maran.questa.network.models.Model
import com.maran.questa.network.models.Model.Question
import com.maran.questa.network.models.Model.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import retrofit2.Retrofit

data class ChoiceState(
    val test: Model.Test? = null,
    val questions: List<Question> = listOf(),
    val numberQuestions: Int = 0,
    val currNumber: Int = 0,
    val isPersonality: Boolean = false,
    val numberCorrect: Int? = null,
    val personalityCount: Map<String, Int> = mapOf()
)

@HiltViewModel
class ChoiceViewModel(
    private val preferences: PreferencesProvider
) : ViewModel() {
    private val coroutineScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private val _uiState = MutableStateFlow(ChoiceState())
    val uiState: StateFlow<ChoiceState> = _uiState.asStateFlow()
    private var retrofit: Retrofit =
        RetrofitClient.retrofitClient(
            preferences.sharedPreferences.getString("token", "") ?: throw UserNotAuthenticatedException()
        )
    private val answerApi: AnswerApi = retrofit.create(AnswerApi::class.java)
    private val questionApi: QuestionApi = retrofit.create(QuestionApi::class.java)
    private val resultApi: ResultApi = retrofit.create(ResultApi::class.java)
    private val roleApi: RoleApi = retrofit.create(RoleApi::class.java)
    private val testApi: TestApi = retrofit.create(TestApi::class.java)
    private val themeApi: ThemeApi = retrofit.create(ThemeApi::class.java)
    private val userApi: UserApi = retrofit.create(UserApi::class.java)

    init {
        coroutineScope.launch {
            val questions = getAllQuestions()
            val newState = _uiState.value.copy(questions = questions)
            updateState(newState)
        }
    }

    fun updateState(newState: ChoiceState) {
        _uiState.update { currentState ->
            currentState.copy(
                test = newState.test,
                questions = newState.questions,
                numberQuestions = newState.numberQuestions,
                currNumber = newState.currNumber, isPersonality = newState.isPersonality,
                numberCorrect = newState.numberCorrect, personalityCount = newState.personalityCount
            )
        }
    }

    fun getNext(): Model {
        return if (_uiState.value.currNumber == _uiState.value.numberQuestions) {
            getResults()
        } else {
            getNextQuestion()
        }
    }

    fun getResults(): Result {
        TODO()
    }

    fun getNextQuestion(): Question =
        _uiState.value.questions.find { curr -> curr.order == _uiState.value.currNumber + 1 }!!

    suspend fun getAllQuestions(): List<Question> {
        return coroutineScope.async {
            questionApi.getAll()
        }.await()
    }
}