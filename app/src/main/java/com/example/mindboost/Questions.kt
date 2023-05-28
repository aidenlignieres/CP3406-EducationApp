package com.example.mindboost

data class Question(
    val id: Int,
    val subject: String,
    val question: String,
    val options: List<String>,
    val correctAnswer: Int
)

object QuestionBank {
    private val easyQuestions: List<Question> = listOf(
        // Easy questions
        Question(
            1,
            "Math",
            "What is the value of pi?",
            listOf("3.14", "2.71", "1.618", "0.5"),
            0
        ),
        Question(
            2,
            "Math",
            "What is the remainder of 21 divided by 7?",
            listOf("21", "7", "1", "None of Above"),
            3
        ),
        Question(
            3,
            "Math",
            "19 + ……. = 42",
            listOf("23", "61", "0", "42"),
            0
        ),
        Question(
            4,
            "Science",
            "What is the chemical symbol for water?",
            listOf("O2", "H2O2", "CO2", "H2O"),
            3
        ),
        Question(
            5,
            "Science",
            "What is the S.I unit of frequency?",
            listOf("Doipter", "Second", "Hertz", "Meter"),
            2
        ),
        Question(
            6,
            "Science",
            "What is the PH range of acids?",
            listOf("0-7", "7-14", "1-7", "7-15"),
            3
        ),
        Question(
            7,
            "Reading",
            "Who is the author of 'To Kill a Mockingbird'?",
            listOf("Harper Lee", "J.K. Rowling", "George Orwell", "Ernest Hemingway"),
            0
        ),
        Question(
            8,
            "Reading",
            "What’s the name of the spiky-headed Sith Lord holding a cool double-blade lightsaber?",
            listOf("Darth Vader", "Darth Maul", "Darth Paul", "Darth Garth"),
            1
        ),
        Question(
            9,
            "Reading",
            "What spell did Harry use to kill Lord Voldemort?",
            listOf("Expelliarmus", "Expecto Patronum", "Avada Kedavra", "Accio"),
            0
        )
        // Add more questions for different subjects
    )

    private val mediumQuestions: List<Question> = listOf(
        // Medium questions
    )

    private val hardQuestions: List<Question> = listOf(
        // Hard questions
    )

    private val usedQuestions: MutableSet<Int> = mutableSetOf()

    fun getQuestionBySubjectAndDifficulty(subject: String, difficulty: String): Question? {
        val questions = when (difficulty) {
            "Easy" -> easyQuestions.filter { it.subject == subject && it.id !in usedQuestions }
            "Medium" -> mediumQuestions.filter { it.subject == subject && it.id !in usedQuestions }
            "Hard" -> hardQuestions.filter { it.subject == subject && it.id !in usedQuestions }
            else -> easyQuestions.filter { it.subject == subject && it.id !in usedQuestions } // Default to Easy questions if difficulty is unknown
        }

        return if (questions.isEmpty()) {
            null
        } else {
            val randomIndex = (questions.indices).random()
            val question = questions[randomIndex]
            usedQuestions.add(question.id)
            question
        }
    }

    fun resetQuestions() {
        usedQuestions.clear()
    }
}
