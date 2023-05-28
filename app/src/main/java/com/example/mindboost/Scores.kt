@file:Suppress("DEPRECATION")

package com.example.mindboost

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.*

data class Score(val subject: String, val difficulty: String, var score: Int, val dateTime: Date)

object Scores {
    private const val MAX_TOP_SCORES = 5
    private val topScores: MutableList<Score> = mutableListOf()
    private val allAttempts: MutableList<Score> = mutableListOf()

    private const val SHARED_PREFS_NAME = "ScoresPrefs"
    private const val KEY_SCORES = "scores"

    private lateinit var sharedPreferences: SharedPreferences
    private val gson = Gson()
    private val scoresType: Type = object : TypeToken<List<Score>>() {}.type

    fun initialize(context: Context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)
        loadScoresFromSharedPreferences()
    }

    private fun loadScoresFromSharedPreferences() {
        val scoresJson = sharedPreferences.getString(KEY_SCORES, null)
        scoresJson?.let {
            val savedScores: List<Score> = gson.fromJson(scoresJson, scoresType)
            allAttempts.addAll(savedScores)
        }
    }

    private fun saveScoresToSharedPreferences() {
        val scoresJson = gson.toJson(allAttempts)
        sharedPreferences.edit().putString(KEY_SCORES, scoresJson).apply()
    }

    fun addScore(subject: String, difficulty: String, score: Int, dateTime: Date) {
        // Check if the score already exists in allAttempts
        val existingScore = allAttempts.find { it.subject == subject && it.difficulty == difficulty && it.dateTime == dateTime }
        if (existingScore != null) {
            // Score already exists, update the score value
            existingScore.score = score
        } else {
            // Score does not exist, add a new score
            val newScore = Score(subject, difficulty, score, dateTime)
            allAttempts.add(newScore)
            updateTopScore(subject, difficulty, newScore)
        }

        saveScoresToSharedPreferences()
    }

    fun getTopScores(): List<Score> {
        return topScores
    }

    fun getAllAttempts(): List<Score> {
        return allAttempts
    }

    private fun updateTopScore(subject: String, difficulty: String, newScore: Score) {
        val existingTopScore = getTopScores().find { it.subject == subject && it.difficulty == difficulty }
        if (existingTopScore != null) {
            topScores.remove(existingTopScore)
        }
        topScores.add(newScore)
        sortTopScores()
        trimTopScores()
    }

    private fun sortTopScores() {
        topScores.sortByDescending { it.score }
    }

    private fun trimTopScores() {
        if (topScores.size > MAX_TOP_SCORES) {
            topScores.removeLast()
        }
    }

    fun calculateAverageResponseTime(scores: List<Score>): Long {
        if (scores.isEmpty()) {
            return 0L
        }

        val totalResponseTime = scores.sumOf { it.score }
        return totalResponseTime / scores.size.toLong()
    }
}
