@file:Suppress("UNREACHABLE_CODE")

package com.example.mindboost.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mindboost.QuestionActivity
import com.example.mindboost.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private lateinit var mathButton: Button
    private lateinit var scienceButton: Button
    private lateinit var readingButton: Button

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.welcomeMessage
        homeViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        mathButton = binding.mathButton
        scienceButton = binding.scienceButton
        readingButton = binding.readingButton

        // Button listeners
        mathButton.setOnClickListener {
            startQuestionActivity("Math")
        }

        scienceButton.setOnClickListener {
            startQuestionActivity("Science")
        }

        readingButton.setOnClickListener {
            startQuestionActivity("Reading")
        }

        return root
    }

    // Activity starter
    private fun startQuestionActivity(subject: String) {
        val intent = Intent(requireActivity(), QuestionActivity::class.java)
        intent.putExtra("subject", subject)
        // println(subject)
        // println(intent)
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}