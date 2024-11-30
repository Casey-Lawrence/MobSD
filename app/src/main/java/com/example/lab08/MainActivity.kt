package com.example.lab08

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import android.util.Log
class MainActivity : AppCompatActivity() {
    private lateinit var reviewDao: ReviewDao
    private lateinit var addReviewLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize DAO
        reviewDao = AppDatabase.getDatabase(applicationContext).reviewDao()

        // Initialize buttons
        val addReviewButton = findViewById<Button>(R.id.addReviewButton)
        val viewPastReviewsButton = findViewById<Button>(R.id.viewPastReviewsButton)

        // Handle Add Review button click
        addReviewButton.setOnClickListener {
            val intent = Intent(this, AddReviewActivity::class.java)
            startActivity(intent)
        }

        // Handle View Past Reviews button click
        viewPastReviewsButton.setOnClickListener {
            val intent = Intent(this, ViewReviewsActivity::class.java)
            startActivity(intent)
        }
    }
}

