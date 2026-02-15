package com.nidoham.bhubishwo.domain

data class QuizQuestion private constructor(
    val title: String,
    val options: List<String>,
    val index: Int,
    val url: String?
) {
    companion object {
        operator fun invoke(
            title: String,
            options: List<String>,
            index: Int,
            url: String? = null
        ): QuizQuestion {
            require(title.isNotBlank()) { "Title cannot be blank" }
            require(options.isNotEmpty()) { "Options cannot be empty" }
            require(options.size == options.distinct().size) { "Options must be unique" }
            require(index in options.indices) { "Index out of range" }

            val correctAnswer = options[index]
            val shuffledOptions = options.shuffled()
            val newIndex = shuffledOptions.indexOf(correctAnswer)

            return QuizQuestion(title, shuffledOptions, newIndex, url)
        }
    }
}