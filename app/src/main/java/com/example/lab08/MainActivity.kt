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
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReviewAdapter
    private lateinit var addReviewLauncher: ActivityResultLauncher<Intent>

    val genres = listOf(
        "Fiction",
        "Non-Fiction",
        "Fantasy",
        "Science Fiction",
        "Mystery",
        "Romance",
        "Horror"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize DAO and RecyclerView
        reviewDao = AppDatabase.getDatabase(applicationContext).reviewDao()
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Register for activity result to handle review addition
        addReviewLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == RESULT_OK) {
                // Refresh the reviews list after a successful addition
                loadReviews()
            }
        }

        // Load reviews when the activity is created
        loadReviews()

        // Button to open AddReviewActivity
        findViewById<Button>(R.id.addReviewButton).setOnClickListener {
            // Launch the AddReviewActivity to add a new review
            val intent = Intent(this, AddReviewActivity::class.java)
            addReviewLauncher.launch(intent)
        }
    }

    // Function to load reviews from the database and display them
    private fun loadReviews() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val reviews = reviewDao.getAllReviews()
                Log.d("MainActivity", "Loaded reviews: $reviews")

                withContext(Dispatchers.Main) {
                    adapter = ReviewAdapter(reviews.toMutableList(), genres) { updatedReview, selectedGenre ->
                        updateReviewInDatabase(updatedReview, selectedGenre)
                    }
                    recyclerView.adapter = adapter
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Failed to load reviews: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }


    // Function to update a review in the database
    private fun updateReviewInDatabase(review: Review, selectedGenre: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val updatedReview = review.copy(genre = selectedGenre) // Update the genre of the review
                reviewDao.update(updatedReview) // Update the review in the database
            } catch (e: Exception) {
                // Handle any errors while updating the review
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Failed to update review: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}