package com.example.mindboost.ui.notifications

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mindboost.Score
import com.example.mindboost.Scores
import com.example.mindboost.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!
    private lateinit var sharedPreferences: SharedPreferences

    private lateinit var previousScoresLayout: LinearLayout

    override fun onAttach(context: Context) {
        super.onAttach(context)
        sharedPreferences = context.getSharedPreferences(
            "com.example.mindboost",
            Context.MODE_PRIVATE
        )
        Scores.initialize(context)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val notificationsViewModel = ViewModelProvider(this)[NotificationsViewModel::class.java]

        val textView: TextView = binding.userStatsTitle
        notificationsViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        // Update total score
        val totalScoreTextView: TextView = binding.totalScoreTextView
        val totalScore = calculateTotalScore()
        totalScoreTextView.text = totalScore.toString()

        // Update average response time
        val averageResponseTimeTextView: TextView = binding.averageResponseTimeTextView
        val scores: List<Score> = Scores.getAllAttempts()
        val averageResponseTime = Scores.calculateAverageResponseTime(scores)
        averageResponseTimeTextView.text = averageResponseTime.toString()

        // Update top scores
        val topScoresLayout: LinearLayout = binding.topScoresLayout
        displayTopScores(topScoresLayout)

        // Update previous scores
        previousScoresLayout = binding.previousScoresLayout
        if (previousScoresLayout.childCount == 0) {
            updatePreviousScores()
        }

        // Set a click listener for the share button
        val shareButton: Button = binding.shareButton
        shareButton.setOnClickListener {
            val resultText = "My result is: $totalScore"
            NotificationUtils.shareResultOnSocialMedia(requireContext(), resultText)
        }

        return root
    }

    private fun calculateTotalScore(): Int {
        val allAttempts = Scores.getAllAttempts()
        return allAttempts.sumOf { it.score }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        previousScoresLayout.removeAllViews()
    }

    @SuppressLint("SetTextI18n")
    private fun updatePreviousScores() {
        previousScoresLayout.removeAllViews()
        val previousScores = Scores.getAllAttempts()
        val numScores = previousScores.size
        val startIndex = if (numScores >= 5) numScores - 5 else 0

        for (i in startIndex until numScores) {
            val score = previousScores[i]
            val scoreTextView = TextView(requireContext())
            scoreTextView.text = "${score.dateTime} -- ${score.subject}: ${score.score}"
            previousScoresLayout.addView(scoreTextView)
            Log.d("NotificationsFragment", i.toString())
        }

        Log.d("NotificationsFragment", "updatePreviousScores function executed")
    }

    @SuppressLint("SetTextI18n")
    private fun displayTopScores(layout: LinearLayout) {
        layout.removeAllViews()

        // Initialize Scores
        Scores.initialize(requireContext())

        val subjects = HashSet<String>()
        val topScores: List<Score> = Scores.getTopScores()

        for (score in topScores) {
            subjects.add(score.subject)
        }

        for (subject in subjects) {
            val topScoreForSubject = topScores.find { it.subject == subject }

            if (topScoreForSubject != null) {
                val textView = TextView(requireContext())
                textView.text = "${topScoreForSubject.dateTime} -- Subject: ${topScoreForSubject.subject}, Score: ${topScoreForSubject.score}"
                layout.addView(textView)
            }
        }
    }
}
