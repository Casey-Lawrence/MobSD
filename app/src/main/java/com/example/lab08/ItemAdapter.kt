package com.example.lab08

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AdapterView
import android.widget.EditText // Use EditText instead of TextView
import android.widget.Spinner
import androidx.recyclerview.widget.RecyclerView

class ReviewAdapter(
    private val reviews: MutableList<Review>,
    private val genres: List<String>, // Pass genres as a parameter
    private val onGenreChanged: (Review, String) -> Unit // Callback for genre change
) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    // ViewHolder class that holds references to each review's views
    class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleEditText: EditText = itemView.findViewById(R.id.titleEditText) // Changed to EditText
        val authorEditText: EditText = itemView.findViewById(R.id.authorEditText) // Changed to EditText
        val genreSpinner: Spinner = itemView.findViewById(R.id.genreSpinner) // Spinner for genre
        val reviewEditText: EditText = itemView.findViewById(R.id.reviewEditText) // Changed to EditText
    }

    // Inflate review item layout and create ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return ReviewViewHolder(itemView)
    }

    // Bind data to views for each review
    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val currentReview = reviews[position]

        // Set text for EditTexts
        holder.titleEditText.setText(currentReview.title)
        holder.authorEditText.setText(currentReview.author)
        holder.reviewEditText.setText(currentReview.review)

        // Set up the Spinner adapter
        val adapter = ArrayAdapter(
            holder.itemView.context,
            android.R.layout.simple_spinner_item,
            genres
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        holder.genreSpinner.adapter = adapter

        // Set the current selection in the Spinner
        holder.genreSpinner.setSelection(genres.indexOf(currentReview.genre))

        // Handle Spinner item selection
        holder.genreSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedGenre = genres[position]
                if (selectedGenre != currentReview.genre) {
                    onGenreChanged(currentReview, selectedGenre) // Trigger callback for genre change
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No-op
            }
        }
    }

    // Return total number of reviews
    override fun getItemCount() = reviews.size
}

