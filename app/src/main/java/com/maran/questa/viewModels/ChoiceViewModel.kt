package com.maran.questa.viewModels

import android.security.keystore.UserNotAuthenticatedException
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.maran.questa.dependencyInjection.PreferencesProvider
import com.maran.questa.network.RetrofitClient
import com.maran.questa.network.apis.AnswerApi
import com.maran.questa.network.apis.QuestionApi
import com.maran.questa.network.apis.ResultApi
import com.maran.questa.network.models.Model
import com.maran.questa.network.models.Model.Answer
import com.maran.questa.network.models.Model.Question
import com.maran.questa.network.models.Model.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import java.util.Stack
import java.util.UUID
import javax.inject.Inject

data class ChoiceState(
    val currQuestion: Question,
    val currNumber: Int = 0,
    val currChosen: Int = -1
)

enum class QuestionStatus {
    NOT_LOADED,
    IN_PROCESS,
    SUCCESS,
    FAILURE
}

@HiltViewModel
class ChoiceViewModel @Inject constructor(
    private val preferences: PreferencesProvider
) : ViewModel() {
    private val coroutineScope: CoroutineScope =
        CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)

    private var retrofit: Retrofit =
        RetrofitClient.retrofitClient(
            preferences.sharedPreferences.getString("token", "")
                ?: throw UserNotAuthenticatedException()
        )
    private val answerApi: AnswerApi = retrofit.create(AnswerApi::class.java)
    private val questionApi: QuestionApi = retrofit.create(QuestionApi::class.java)
    private val resultApi: ResultApi = retrofit.create(ResultApi::class.java)


    private var questions: List<Question> = listOf()
    private val answers: MutableList<List<Answer>> = mutableListOf()
    private var isPersonality: Boolean = false
    private val personalityCount: Map<String, Int> = mapOf()

    private val stack = Stack<ChoiceState>()
    val currQuestion = mutableStateOf<Question?>(null)
    val currAnswers = mutableStateOf<List<Answer>>(listOf())
    val currNumber = mutableIntStateOf(0)
    val currChosen = mutableIntStateOf(-1)
    var numberQuestions: Int = 0
    val isPrevious = mutableStateOf(currNumber.intValue > 1)
    var questionStatus by mutableStateOf(QuestionStatus.NOT_LOADED)

    fun initialize(testId: UUID, isPerson: Boolean) {
        val job = coroutineScope.launch {
            isPersonality = isPerson
            questions = getAllQuestions(testId)
            if (questionStatus == QuestionStatus.SUCCESS) {
                currQuestion.value = questions[0]
                numberQuestions = questions.size
                currNumber.intValue = 1
                currAnswers.value = getAnswers(currQuestion.value!!.id)
                answers.add(currAnswers.value)
                for (i in 1..<questions.size) {
                    answers.add(getAnswers(questions[i].id))
                }
            }
        }
    }

    fun updateState(question: Question, answers: List<Answer>, number: Int, chosen: Int) {
        currQuestion.value = question
        currAnswers.value = answers
        currNumber.intValue = number
        currChosen.value = chosen
    }

    fun getNext(): Model {
        return if (currNumber.intValue == numberQuestions) {
            getResults()
        } else {
            updateState(
                question = questions[currNumber.intValue],
                answers = answers[currNumber.intValue],
                number = currNumber.intValue + 1,
                chosen = -1
            )
            questions[currNumber.intValue - 1]
        }
    }

    fun getPrevious() {
        if (currNumber.intValue > 1) {
            stack.pop()
            val state = stack.peek()
            updateState(
                state.currQuestion,
                answers[state.currNumber - 1],
                number = state.currNumber,
                chosen = state.currChosen
            )
        }
    }

    fun getResults(): Result {
        TODO()
    }

    suspend fun getAllQuestions(testId: UUID): List<Question> {
        questionStatus = QuestionStatus.IN_PROCESS
        return coroutineScope.async {
            questionApi.getByTest(testId).onSuccess {
                questionStatus = QuestionStatus.SUCCESS
            }.onFailure {
                questionStatus = QuestionStatus.FAILURE
            }.getOrDefault(listOf())
        }.await()
    }

    suspend fun getAnswers(id: UUID): List<Answer> {
        return coroutineScope.async {
            answerApi.getByQuestion(id).getOrDefault(listOf())
        }.await()
    }

    override fun onCleared() {
        coroutineScope.cancel()
    }
}