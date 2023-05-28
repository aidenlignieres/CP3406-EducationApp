package com.example.mindboost

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
//import android.util.Log
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class QuestionActivity : AppCompatActivity() {

    private var currentQuestion: Question? = null
    private var questionStartTime: Long = 0L
    private var score: Int = 0

    private lateinit var subject: String
    private lateinit var difficulty: String
    private lateinit var dateTime: Date

    private lateinit var questionTextView: TextView
    private lateinit var optionContainer: LinearLayout

    private lateinit var scoreTextView: TextView

    private lateinit var sharedPreferences: SharedPreferences

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.event_screen)

        sharedPreferences = getSharedPreferences("com.example.mindboost", Context.MODE_PRIVATE)
        Scores.initialize(this)

        // Retrieve the subject from the intent
        subject = intent.getStringExtra("subject") ?: ""
        difficulty = intent.getStringExtra("difficulty") ?: ""
        println(subject)

        // Retrieve the first question for the subject
        currentQuestion = subject.let { QuestionBank.getQuestionBySubjectAndDifficulty(it, difficulty) }

        questionTextView = findViewById(R.id.questionTextView)
        optionContainer = findViewById(R.id.optionContainer)

        scoreTextView = findViewById(R.id.scoreValueTextView)
        scoreTextView.text = getString(R.string.current_score_value)

        //Log.d("QuestionActivity", "Current question: $currentQuestion")
        if (currentQuestion != null) {
            displayQuestion(currentQuestion!!)
            //println("True")
        } else {
            //println("False")
            // No questions available for the subject
            // Handle the situation accordingly
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun displayQuestion(question: Question) {
        // Format the question object
        // val formattedQuestion = "Question(id=${question.id}, subject=${question.subject}, " +
        //        "question=${question.question}, options=${question.options}, " +
        //        "correctAnswer=${question.correctAnswer})"

        // Display the formatted question in the UI
        questionTextView.text = question.question

        // Clear any previously added options
        optionContainer.removeAllViews()

        // Add options dynamically
        for (optionIndex in question.options.indices) {
            val optionTextView = TextView(this).apply {
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    topMargin = resources.getDimensionPixelSize(R.dimen.option_margin_top)
                }
                text = question.options[optionIndex]
                setOnClickListener {
                    checkAnswer(optionIndex)
                }
            }
            optionContainer.addView(optionTextView)
        }

        // Start the timer for response time
        questionStartTime = System.currentTimeMillis()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun checkAnswer(selectedAnswer: Int) {
        val elapsedTime = System.currentTimeMillis() - questionStartTime

        // Check if the selected answer is correct
        val correctAnswer = currentQuestion?.correctAnswer
        val isCorrect = correctAnswer == selectedAnswer

        // Update the score based on response time if the answer is correct
        score += if (isCorrect) {
            // Calculate the score based on response time
            val responseTimeScore = calculateResponseTimeScore(elapsedTime)
            responseTimeScore
        } else {
            0

        }

        // Display the correct answer
        displayCorrectAnswer(correctAnswer)
        updateScore()

        // Wait for 2 seconds before moving to the next question
        Handler(Looper.getMainLooper()).postDelayed({
            getNextQuestion()
        }, 2000)
    }

    private fun calculateResponseTimeScore(elapsedTime: Long): Int {
        // Calculate the score based on the elapsed time
        // Adjust the scoring algorithm according to your requirements
        val maxScore = 100
        val deductionPerSecond = 1
        val elapsedSeconds = (elapsedTime / 100).toInt()
        val score = maxScore - deductionPerSecond * elapsedSeconds
        return if (score <= 0) {
            0
        } else {
            score
        }
    }

    private fun displayCorrectAnswer(correctAnswer: Int?) {
        // Display the correct answer in the UI
        for (i in 0 until optionContainer.childCount) {
            val optionTextView = optionContainer.getChildAt(i) as TextView
            if (i == correctAnswer) {
                optionTextView.setTextColor(Color.GREEN)
            } else {
                optionTextView.setTextColor(Color.BLACK)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun getNextQuestion() {
        // Retrieve the next question for the subject
        val subject = currentQuestion?.subject
        currentQuestion = subject?.let { QuestionBank.getQuestionBySubjectAndDifficulty(it, difficulty) }

        if (currentQuestion != null) {
            displayQuestion(currentQuestion!!)
        } else {
            // No more questions available for the subject
            // Save the score and handle the end of the quiz
            saveScoreAndFinish()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun saveScoreAndFinish() {
        // Save the score with the date and time to a local storage or database
        dateTime = Date()

        // Handle the score data according to your requirements
        // You can save it locally, display it to the user, etc.
        Scores.addScore(subject, difficulty, score, dateTime)

        // Reset the score to 0
        score = 0

        // Reset the currentQuestion and questionStartTime variables
        QuestionBank.resetQuestions()

        // Finish the activity
        finish()
    }

    private fun updateScore() {
        val scoreString = score.toString()
        scoreTextView.text = scoreString
    }
}
