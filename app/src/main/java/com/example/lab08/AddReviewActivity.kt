package com.example.lab08

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.app.Activity
import android.util.Log
import kotlinx.coroutines.withContext

class AddReviewActivity : AppCompatActivity() {
    private lateinit var reviewDao: ReviewDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_item)

        // Initialize the DAO
        reviewDao = AppDatabase.getDatabase(applicationContext).reviewDao()

        // Initialize input fields and button
        val titleEditText = findViewById<EditText>(R.id.titleEditText)
        val authorEditText = findViewById<EditText>(R.id.authorEditText)
        val genreSpinner = findViewById<Spinner>(R.id.genreSpinner)
        val reviewEditText = findViewById<EditText>(R.id.reviewEditText)
        val saveButton = findViewById<Button>(R.id.saveButton)

        // Set up the spinner with genres
        val genres = listOf(
            "Fiction",
            "Non-Fiction",
            "Fantasy",
            "Science Fiction",
            "Mystery",
            "Romance",
            "Horror"
        )
        val genreAdapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            genres
        )
        genreAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genreSpinner.adapter = genreAdapter

        saveButton.setOnClickListener {
            val title = titleEditText.text.toString()
            val author = authorEditText.text.toString()
            val genre = genreSpinner.selectedItem.toString()
            val review = reviewEditText.text.toString()

            if (title.isBlank() || author.isBlank() || review.isBlank()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
            } else {
                CoroutineScope(Dispatchers.IO).launch {
                    val newReview = Review(
                        title = title,
                        author = author,
                        genre = genre,
                        review = review
                    )
                    Log.d("AddReviewActivity", "Inserting Review: $newReview")
                    reviewDao.insert(newReview)
                    Log.d("AddReviewActivity", "Review inserted into the database.")

                    withContext(Dispatchers.Main) {
                        setResult(Activity.RESULT_OK)
                        finish()
                    }
                }
            }
        }

    }
}
