package za.co.howtogeek.geoquiz

import android.util.Log
import androidx.annotation.StringRes
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

private const val TAG = "QuizViewModel"
const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"


class QuizViewModel(private val savedStateHandle: SavedStateHandle) : ViewModel() {

    private val _questionBank = MutableStateFlow(
        listOf(
            Question(R.string.question_australia, true),
            Question(R.string.question_oceans, true),
            Question(R.string.question_mideast, false),
            Question(R.string.question_africa, false),
            Question(R.string.question_americas, true),
            Question(R.string.question_asia, true)
        )
    )
    val questionBank: StateFlow<List<Question>> = _questionBank.asStateFlow()
    val currentIndex: StateFlow<Int> = savedStateHandle.getStateFlow(CURRENT_INDEX_KEY, 0)

    private val _answeredQuestionsCount = MutableStateFlow(0)
    val answeredQuestionsCount: StateFlow<Int> = _answeredQuestionsCount.asStateFlow()

    private val _correctAnswersCount = MutableStateFlow(0)
    val correctAnswersCount: StateFlow<Int> = _correctAnswersCount.asStateFlow()


    private val _quizFinished = MutableStateFlow(false)
    val quizFinished: StateFlow<Boolean> = _quizFinished.asStateFlow()

    val currentQuestion: StateFlow<Question?> = combine(currentIndex, questionBank){index, questions ->
        questions.getOrNull(index)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = _questionBank.value.first()
    )

    fun checkAnswer(userAnswer: Boolean): Int {
        val correctAnswer = currentQuestion.value?.answer ?: false
        val messageResId = if (userAnswer == correctAnswer) {
            _correctAnswersCount.value++
            R.string.correct_toast
            //Log.d(TAG, "Correct Answers: ${_correctAnswersCount.value} out of ${_questionBank.value.size}")
        } else {
            R.string.incorrect_toast
            //Log.d(TAG, "Incorrect Answers: ${_correctAnswersCount.value.-_questionBank.value.size} out of ${_questionBank.value.size}")
        }
        markQuestionAsAnswered()
        return messageResId
    }

    // This function will update the 'answered' status
    private fun markQuestionAsAnswered() {
        val currentQuestions = _questionBank.value.toMutableList()
        val questionIndex = currentIndex.value
        val question = currentQuestions.getOrNull(questionIndex)

        if (question != null && !question.answered) {
            // Create a copy of the question with answered = true
            currentQuestions[questionIndex] = question.copy(answered = true)
            // Update the state flow with the new list
            _questionBank.value = currentQuestions
            _answeredQuestionsCount.value++
            if (_answeredQuestionsCount.value == _questionBank.value.size) {
                _quizFinished.value = true
            }
        }
    }

    fun moveToNext() {
        savedStateHandle[CURRENT_INDEX_KEY] = (currentIndex.value + 1) % _questionBank.value.size
    }

    fun moveToPrevious(){
        savedStateHandle[CURRENT_INDEX_KEY] = (currentIndex.value - 1 + _questionBank.value.size) % questionBank.value.size
    }

}