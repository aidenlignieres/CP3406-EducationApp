package com.example.mindboost

data class Question(
    val id: Int,
    val subject: String,
    val question: String,
    val options: List<String>,
    val correctAnswer: Int
)

object QuestionBank {
    private val allQuestions: List<Question> = listOf(
        // place holder
        Question(
            1,
            "Math",
            "What is the value of pi?",
            listOf("3.14", "2.71", "1.618", "0.5"),
            0
        ),
        // place holder
        Question(
            2,
            "Science",
            "What is the chemical symbol for water?",
            listOf("O2", "H2O2", "CO2", "H2O"),
            3
        ),
        // place holders
        Question(
            3,
            "Reading",
            "Who is the author of 'To Kill a Mockingbird'?",
            listOf("Harper Lee", "J.K. Rowling", "George Orwell", "Ernest Hemingway"),
            0
        )
        // Add more questions for different subjects
    )

    private val usedQuestions: MutableSet<Int> = mutableSetOf()

    fun getQuestionBySubject(subject: String): Question? {
        val questions = allQuestions.filter { it.subject == subject && it.id !in usedQuestions }
        return if (questions.isEmpty()) {
            null
        } else {
            val randomIndex = (questions.indices).random()
            val question = questions[randomIndex]
            usedQuestions.add(question.id)
            question
        }
    }

    fun resetUsedQuestions() {
        usedQuestions.clear()
    }
}
