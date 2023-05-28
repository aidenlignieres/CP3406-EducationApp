package com.example.mindboost.ui.dashboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.mindboost.R
import com.example.mindboost.databinding.FragmentDashboardBinding

class DashboardFragment : Fragment() {

    private var _binding: FragmentDashboardBinding? = null
    private lateinit var selectedDifficulty: String

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this)[DashboardViewModel::class.java]

        _binding = FragmentDashboardBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.settingsTitle
        dashboardViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val difficultyRadioGroup: RadioGroup = root.findViewById(R.id.difficultyRadioGroup)

        difficultyRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            val selectedRadioButton: RadioButton = group.findViewById(checkedId)
            selectedDifficulty = selectedRadioButton.text.toString()
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getSelectedDifficulty(): String {
        return selectedDifficulty
    }
}
