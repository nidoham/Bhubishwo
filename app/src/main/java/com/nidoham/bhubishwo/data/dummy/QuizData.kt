package com.nidoham.bhubishwo.data.dummy

import com.nidoham.bhubishwo.domain.QuizQuestion

object QuizData {
    fun getSampleQuestions(): List<QuizQuestion> = listOf(
        QuizQuestion(
            title = "What is the capital of Myanmar?",
            options = listOf("Naypyidaw", "Phnom Penh", "Ulaanbaatar", "Dili"),
            index = 0
        ),
        QuizQuestion(
            title = "What is the capital of Cambodia?",
            options = listOf("Bangkok", "Phnom Penh", "Vientiane", "Hanoi"),
            index = 1
        ),
        QuizQuestion(
            title = "What is the capital of Mongolia?",
            options = listOf("Ulaanbaatar", "Astana", "Bishkek", "Tashkent"),
            index = 0
        ),
        QuizQuestion(
            title = "What is the capital of East Timor?",
            options = listOf("Dili", "Darwin", "Port Moresby", "Suva"),
            index = 0
        ),
        QuizQuestion(
            title = "What is the capital of Thailand?",
            options = listOf("Bangkok", "Hanoi", "Manila", "Jakarta"),
            index = 0,
            url = "https://en.wikipedia.org/wiki/Bangkok"
        ),
        QuizQuestion(
            title = "What is the capital of Vietnam?",
            options = listOf("Ho Chi Minh City", "Hanoi", "Da Nang", "Hue"),
            index = 1
        ),
        QuizQuestion(
            title = "What is the capital of Laos?",
            options = listOf("Vientiane", "Luang Prabang", "Pakse", "Savannakhet"),
            index = 0
        ),
        QuizQuestion(
            title = "What is the capital of Philippines?",
            options = listOf("Cebu", "Davao", "Manila", "Quezon City"),
            index = 2
        )
    )
}