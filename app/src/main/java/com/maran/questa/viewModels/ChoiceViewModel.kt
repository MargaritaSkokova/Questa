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
import com.maran.questa.network.models.Model.Answer
import com.maran.questa.network.models.Model.Question
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
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
    val currChosen: Int = -1,
    val currAnswers: List<Answer> = listOf()
)

enum class QuestionStatus {
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
    private lateinit var answers: MutableList<Deferred<List<Answer>>?>
    private var isPersonality: Boolean = false
    private val personalityCount: MutableMap<String, Int> = mutableMapOf()
    private var countCorrect = 0

    private val stack = Stack<ChoiceState>()
    val currQuestion = mutableStateOf<Question?>(null)
    val currAnswers = mutableStateOf<List<Answer>>(listOf())
    val currNumber = mutableIntStateOf(0)
    val currChosen = mutableIntStateOf(-1)
    val show = mutableStateOf(false)
    var numberQuestions: Int = 0
    val isPrevious = mutableStateOf(false)
    var questionStatus by mutableStateOf(QuestionStatus.IN_PROCESS)

    fun initialize(testId: UUID, isPerson: Boolean) {
        coroutineScope.launch {
            isPersonality = isPerson
            questions = getAllQuestions(testId)
            if (questions.isEmpty()) {
                questionStatus = QuestionStatus.FAILURE
            } else {
                currQuestion.value = questions[0]
                numberQuestions = questions.size
                answers = MutableList(numberQuestions) { null }
                currNumber.intValue = 1
                answers[0] = getAnswers(currQuestion.value!!.id)
                currAnswers.value = answers[0]?.await() ?: listOf()
                questionStatus = QuestionStatus.SUCCESS
                for (i in 1..<questions.size) {
                    answers[i] = getAnswers(questions[i].id)
                }
            }
        }
    }

    fun updateState(question: Question, answers: List<Answer>, number: Int, chosen: Int) {
        currQuestion.value = question
        currAnswers.value = answers
        currNumber.intValue = number
        currChosen.intValue = chosen
    }

    fun getNext() {
        if (currNumber.intValue == numberQuestions) {
            getResults()
        } else {
            stack.push(
                ChoiceState(
                    currQuestion = currQuestion.value!!,
                    currNumber = currNumber.intValue,
                    currChosen = currChosen.intValue,
                    currAnswers = currAnswers.value
                )
            )
            questionStatus = QuestionStatus.IN_PROCESS
            coroutineScope.launch {
                show.value = false
                val answer = answers[currNumber.intValue]?.await()
                if (answer == null) {
                    questionStatus = QuestionStatus.FAILURE
                    return@launch
                }

                if (isPersonality) {
                    if (currAnswers.value[currChosen.intValue].personality != null)
                        personalityCount[currAnswers.value[currChosen.intValue].personality!!] =
                            personalityCount.getOrDefault(
                                currAnswers.value[currChosen.intValue].personality,
                                0
                            ) + 1
                } else {
                    if (currAnswers.value[currChosen.intValue].isCorrect != null && currAnswers.value[currChosen.intValue].isCorrect!!) {
                        countCorrect ++
                    }
                }

                updateState(
                    question = questions[currNumber.intValue],
                    answers = answer,
                    number = currNumber.intValue + 1,
                    chosen = -1
                )

                isPrevious.value = currNumber.intValue > 1

                questionStatus = QuestionStatus.SUCCESS
            }
        }
    }

    fun getPrevious() {
        if (currNumber.intValue > 1) {
            val state = stack.peek()

            if (isPersonality) {
                if (state.currAnswers[state.currChosen].personality != null)
                    personalityCount[state.currAnswers[state.currChosen].personality!!] =
                        personalityCount.getOrDefault(
                            state.currAnswers[state.currChosen].personality,
                            0
                        ) - 1
            } else {
                if (state.currAnswers[state.currChosen].isCorrect != null && state.currAnswers[state.currChosen].isCorrect!!) {
                    countCorrect --
                }
            }

            updateState(
                state.currQuestion,
                state.currAnswers,
                number = state.currNumber,
                chosen = state.currChosen
            )
            show.value = true
            isPrevious.value = currNumber.intValue > 1
            stack.pop()
        }
    }

    fun getResults() {
        TODO()
    }

    suspend fun getAllQuestions(testId: UUID): List<Question> {
        questionStatus = QuestionStatus.IN_PROCESS
        return coroutineScope.async {
            questionApi.getByTest(testId).getOrDefault(listOf())
        }.await()
    }

    fun getAnswers(id: UUID): Deferred<List<Answer>> {
        return coroutineScope.async {
            answerApi.getByQuestion(id).getOrDefault(listOf())
        }
    }

    override fun onCleared() {
        coroutineScope.cancel()
    }
}