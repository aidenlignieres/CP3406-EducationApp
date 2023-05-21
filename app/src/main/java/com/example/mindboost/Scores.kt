package com.example.mindboost

import java.time.LocalDateTime

data class Score(val subject: String, val score: Int, val dateTime: LocalDateTime)

object Scores {
    private const val MAX_TOP_SCORES = 5
    private val topScores: MutableList<Score> = mutableListOf()
    private val allAttempts: MutableList<Score> = mutableListOf()

    fun addScore(subject: String, score: Int, dateTime: LocalDateTime) {
        val newScore = Score(subject, score, dateTime)
        allAttempts.add(newScore)

        val currentTopScore = getTopScoreForSubject(subject)
        if (currentTopScore == null || score > currentTopScore.score) {
            updateTopScore(subject, newScore)
        }
    }

    private fun getTopScoreForSubject(subject: String): Score? {
        return topScores.find { it.subject == subject }
    }

    fun getAllAttemptsForSubject(subject: String): List<Score> {
        return allAttempts.filter { it.subject == subject }
    }

    private fun updateTopScore(subject: String, newScore: Score) {
        val existingTopScore = getTopScoreForSubject(subject)
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
}
