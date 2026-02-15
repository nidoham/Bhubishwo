package com.nidoham.bhubishwo.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nidoham.bhubishwo.data.dummy.QuizData
import com.nidoham.bhubishwo.domain.QuizQuestion
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class GameUiState(
    val currentQuestionIndex: Int = 0,
    val questions: List<QuizQuestion> = emptyList(),
    val userScore: Int = 0,
    val selectedAnswer: String? = null,
    val isAnswerSubmitted: Boolean = false,
    val gameFinished: Boolean = false,
    val questionStartTime: Long = 0L,
    val timeRemaining: Int = 20,
    val showCorrectAnswer: Boolean = false,
    val showPerformanceDialog: Boolean = false,
    val performanceStats: PerformanceStats = PerformanceStats(),
    val answeredQuestions: List<AnsweredQuestion> = emptyList()
)

data class PerformanceStats(
    val totalCorrect: Int = 0,
    val totalWrong: Int = 0,
    val totalTimeBonus: Int = 0,
    val basePoints: Int = 0,
    val penaltyPoints: Int = 0,
    val averageResponseTime: Float = 0f,
    val fastestAnswer: Int = 20,
    val slowestAnswer: Int = 0
)

data class AnsweredQuestion(
    val questionTitle: String,
    val selectedAnswer: String,
    val correctAnswer: String,
    val isCorrect: Boolean,
    val responseTimeSeconds: Int,
    val timeBonus: Int,
    val pointsEarned: Int
)

class GameViewModel : ViewModel() {

    companion object {
        const val CORRECT_POINTS = 10
        const val WRONG_PENALTY = -5
        const val QUESTION_TIME_LIMIT = 20
    }

    private val _uiState = MutableStateFlow(GameUiState())
    val uiState: StateFlow<GameUiState> = _uiState.asStateFlow()

    private var timerJob: Job? = null

    init {
        loadQuestions()
    }

    private fun loadQuestions() {
        val questions = QuizData.getSampleQuestions()
        _uiState.update {
            it.copy(
                questions = questions,
                questionStartTime = System.currentTimeMillis(),
                timeRemaining = QUESTION_TIME_LIMIT
            )
        }
        startTimer()
    }

    private fun startTimer() {
        timerJob?.cancel()

        timerJob = viewModelScope.launch {
            _uiState.update { it.copy(timeRemaining = QUESTION_TIME_LIMIT, showCorrectAnswer = false) }

            // Countdown from 20 to 0 (every second)
            for (time in QUESTION_TIME_LIMIT downTo 0) {
                _uiState.update { it.copy(timeRemaining = time) }
                if (time > 0) {
                    delay(1000)
                }
            }

            // Time's up
            val currentState = _uiState.value
            if (!currentState.isAnswerSubmitted) {
                handleTimeOut()
            }
        }
    }

    private fun handleTimeOut() {
        val currentState = _uiState.value
        val currentQuestion = currentState.questions.getOrNull(currentState.currentQuestionIndex)
            ?: return

        val answeredQuestion = AnsweredQuestion(
            questionTitle = currentQuestion.title,
            selectedAnswer = "Time Out",
            correctAnswer = currentQuestion.options[currentQuestion.index],
            isCorrect = false,
            responseTimeSeconds = QUESTION_TIME_LIMIT,
            timeBonus = 0,
            pointsEarned = WRONG_PENALTY
        )

        _uiState.update { current ->
            current.copy(
                isAnswerSubmitted = true,
                showCorrectAnswer = true,
                selectedAnswer = null,
                userScore = (current.userScore + WRONG_PENALTY).coerceAtLeast(0),
                answeredQuestions = current.answeredQuestions + answeredQuestion
            )
        }

        // Show correct answer for 2 seconds then next
        viewModelScope.launch {
            delay(2000)
            nextQuestion()
        }
    }

    fun selectAnswer(answer: String) {
        if (!_uiState.value.isAnswerSubmitted) {
            _uiState.update { it.copy(selectedAnswer = answer) }
        }
    }

    fun submitAnswer() {
        val currentState = _uiState.value
        if (currentState.isAnswerSubmitted) return

        val currentQuestion = currentState.questions.getOrNull(currentState.currentQuestionIndex)
            ?: return

        timerJob?.cancel()

        val endTime = System.currentTimeMillis()
        val responseTimeMs = endTime - currentState.questionStartTime
        val responseTimeSeconds = (responseTimeMs / 1000).toInt().coerceIn(0, QUESTION_TIME_LIMIT)

        val isCorrect = currentState.selectedAnswer == currentQuestion.options[currentQuestion.index]

        var pointsEarned = 0
        var timeBonus = 0

        if (isCorrect) {
            pointsEarned += CORRECT_POINTS
            timeBonus = when (responseTimeSeconds) {
                in 0..2 -> 1
                in 3..4 -> 2
                in 5..6 -> 3
                in 7..8 -> 4
                in 9..10 -> 5
                in 11..12 -> 6
                in 13..14 -> 7
                in 15..16 -> 8
                in 17..18 -> 9
                in 19..20 -> 10
                else -> 0
            }
            pointsEarned += timeBonus
        } else {
            pointsEarned = WRONG_PENALTY
        }

        val answeredQuestion = AnsweredQuestion(
            questionTitle = currentQuestion.title,
            selectedAnswer = currentState.selectedAnswer ?: "No Answer",
            correctAnswer = currentQuestion.options[currentQuestion.index],
            isCorrect = isCorrect,
            responseTimeSeconds = responseTimeSeconds,
            timeBonus = timeBonus,
            pointsEarned = pointsEarned
        )

        _uiState.update { current ->
            current.copy(
                isAnswerSubmitted = true,
                showCorrectAnswer = true,
                userScore = (current.userScore + pointsEarned).coerceAtLeast(0),
                answeredQuestions = current.answeredQuestions + answeredQuestion
            )
        }

        viewModelScope.launch {
            delay(2000)
            nextQuestion()
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
                    isAnswerSubmitted = false,
                    showCorrectAnswer = false,
                    questionStartTime = System.currentTimeMillis()
                )
            }
            startTimer() // Restart timer for next question
        } else {
            calculatePerformanceStats()
            _uiState.update {
                it.copy(
                    gameFinished = true,
                    showPerformanceDialog = true
                )
            }
        }
    }

    private fun calculatePerformanceStats() {
        val answered = _uiState.value.answeredQuestions
        if (answered.isEmpty()) return

        val correctAnswers = answered.filter { it.isCorrect }
        val wrongAnswers = answered.filter { !it.isCorrect }

        val totalTimeBonus = correctAnswers.sumOf { it.timeBonus }
        val basePoints = correctAnswers.size * CORRECT_POINTS
        val penaltyPoints = wrongAnswers.size * WRONG_PENALTY

        val avgTime = answered.map { it.responseTimeSeconds }.average().toFloat()
        val fastest = answered.minOfOrNull { it.responseTimeSeconds } ?: QUESTION_TIME_LIMIT
        val slowest = answered.maxOfOrNull { it.responseTimeSeconds } ?: 0

        _uiState.update {
            it.copy(
                performanceStats = PerformanceStats(
                    totalCorrect = correctAnswers.size,
                    totalWrong = wrongAnswers.size,
                    totalTimeBonus = totalTimeBonus,
                    basePoints = basePoints,
                    penaltyPoints = penaltyPoints,
                    averageResponseTime = avgTime,
                    fastestAnswer = fastest,
                    slowestAnswer = slowest
                )
            )
        }
    }

    fun dismissPerformanceDialog() {
        _uiState.update { it.copy(showPerformanceDialog = false) }
    }

    fun exitGame() {
        timerJob?.cancel()
        _uiState.update { GameUiState() }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
    }
}