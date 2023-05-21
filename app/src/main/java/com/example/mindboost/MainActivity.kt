package com.example.mindboost

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var bottomNavigationView: BottomNavigationView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomNavigationView = findViewById(R.id.nav_view)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Connect NavController to BottomNavigationView
        NavigationUI.setupWithNavController(bottomNavigationView, navController)

        // Button listeners
        val mathButton = findViewById<Button>(R.id.mathButton)
        mathButton.setOnClickListener {
            startQuestionActivity("Math")
        }

        val scienceButton = findViewById<Button>(R.id.scienceButton)
        scienceButton.setOnClickListener {
            startQuestionActivity("Science")
        }

        val readingButton = findViewById<Button>(R.id.readingButton)
        readingButton.setOnClickListener {
            startQuestionActivity("Reading")
        }
    }

    // Activity starter
    private fun startQuestionActivity(subject: String) {
        val intent = Intent(this, QuestionActivity::class.java)
        intent.putExtra("subject", subject)
        startActivity(intent)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}

