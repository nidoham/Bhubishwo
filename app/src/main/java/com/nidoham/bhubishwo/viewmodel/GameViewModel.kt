package com.nidoham.bhubishwo.viewmodel

import androidx.lifecycle.ViewModel
import com.nidoham.bhubishwo.data.dummy.QuizData
import com.nidoham.bhubishwo.domain.QuizQuestion
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class GameUiState(
    val currentQuestionIndex: Int = 0,
    val questions: List<QuizQuestion> = emptyList(),
    val userScore: Int = 0,
    val selectedAnswer: String? = null,
    val isAnswerSubmitted: Boolean = false,
    val gameFinished: Boolean = false
)

class GameViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    init {
        loadQuestions()
    }

    private fun loadQuestions() {
        _uiState.update { it.copy(questions = QuizData.getSampleQuestions()) }
    }

    fun selectAnswer(answer: String) {
        if (!_uiState.value.isAnswerSubmitted) {
            _uiState.update { it.copy(selectedAnswer = answer) }
        }
    }

    fun submitAnswer() {
        val currentState = _uiState.value
        val currentQuestion = currentState.questions.getOrNull(currentState.currentQuestionIndex)

        if (currentQuestion != null && currentState.selectedAnswer == currentQuestion.options[currentQuestion.index]) {
            _uiState.update {
                it.copy(
                    userScore = it.userScore + 1,
                    isAnswerSubmitted = true
                )
            }
        } else {
            _uiState.update { it.copy(isAnswerSubmitted = true) }
        }
    }

    fun nextQuestion() {
        val currentState = _uiState.value
        val nextIndex = currentState.currentQuestionIndex + 1

        if (nextIndex < currentState.questions.size) {
            _uiState.update {
                it.copy(
                    currentQuestionIndex = nextIndex,
                    selectedAnswer = null,
                    isAnswerSubmitted = false
                )
            }
        } else {
            _uiState.update { it.copy(gameFinished = true) }
        }
    }

    fun restartGame() {
        _uiState.update {
            GameUiState(
                questions = QuizData.getSampleQuestions()
            )
        }
    }
}