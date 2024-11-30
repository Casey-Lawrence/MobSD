package com.example.lab08

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewReviewsActivity : AppCompatActivity() {
    private lateinit var reviewDao: ReviewDao
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ReviewAdapter

    // List of available genres for the spinner
    private val genres = listOf(
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
        setContentView(R.layout.activity_view_reviews)

        // Initialize DAO
        reviewDao = AppDatabase.getDatabase(applicationContext).reviewDao()

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Attach an empty adapter to RecyclerView initially
        //I got this code from chat gpt as i got this error  E  No adapter attached; skipping layout
        // So i asked chat gpt why this error was ocurring and it said that my
        //Recycler View didnt have an adapter set when layout was being drawn
        //This is likely because the adapter is being initialized asynchronously
        // but the RecyclerView tries to render before the adapter is set.
        //so it said to ensure the RecyclerView is fully initialized on the main thread
        // before loading reviews.
        adapter = ReviewAdapter(mutableListOf(), genres) { _, _ -> } // Empty adapter
        recyclerView.adapter = adapter
        // Load reviews from the database
        loadReviews()
    }

    private fun loadReviews() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val reviews = reviewDao.getAllReviews() // Fetch all reviews
                withContext(Dispatchers.Main) {
                    // Initialize the adapter and pass genres + callback for updates
                    adapter = ReviewAdapter(reviews.toMutableList(), genres) { updatedReview, selectedGenre ->
                        updateReviewInDatabase(updatedReview, selectedGenre)
                    }
                    recyclerView.adapter = adapter // Attach adapter to RecyclerView
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Failed to load reviews: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    private fun updateReviewInDatabase(review: Review, selectedGenre: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val updatedReview = review.copy(genre = selectedGenre) // Update the genre
                reviewDao.update(updatedReview) // Update the review in the database
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(applicationContext, "Failed to update review: ${e.message}", Toast.LENGTH_LONG).show()
                }
            }
        }
    }
}
