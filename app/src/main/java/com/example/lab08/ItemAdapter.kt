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
    private val genres: List<String>, // List of available genres
    private val onGenreChanged: (Review, String) -> Unit // Callback for genre change
) : RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder>() {

    fun updateReviews(newReviews: List<Review>) {
        reviews.clear()
        reviews.addAll(newReviews)
        notifyDataSetChanged()
    }

    class ReviewViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titleEditText: EditText = itemView.findViewById(R.id.titleEditText)
        val authorEditText: EditText = itemView.findViewById(R.id.authorEditText)
        val genreSpinner: Spinner = itemView.findViewById(R.id.genreSpinner)
        val reviewEditText: EditText = itemView.findViewById(R.id.reviewEditText)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_view, parent, false)
        return ReviewViewHolder(itemView)
    }


    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        val currentReview = reviews[position]

        // Bind review data to UI components
        holder.titleEditText.setText(currentReview.title)
        holder.authorEditText.setText(currentReview.author)
        holder.reviewEditText.setText(currentReview.review)

        // Set up the spinner adapter
        val adapter = ArrayAdapter(
            holder.itemView.context,
            android.R.layout.simple_spinner_item,
            genres
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        holder.genreSpinner.adapter = adapter

        // Set the spinner selection to the current genre
        holder.genreSpinner.setSelection(genres.indexOf(currentReview.genre))

        // Handle genre selection changes
        holder.genreSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val selectedGenre = genres[position]
                if (selectedGenre != currentReview.genre) {
                    onGenreChanged(currentReview, selectedGenre) // Notify parent of the change
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // No action needed
            }
        }
    }

    override fun getItemCount() = reviews.size
}
