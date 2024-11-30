package com.example.lab08
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface ReviewDao{
    //    Insert a new review
    @Insert
    suspend fun insert(review: Review)

    //    update a review
    @Update
    suspend fun update(review: Review)

    //    view all reviews
    @Query("SELECT * FROM reviews")
    suspend fun getAllReviews(): List<Review>
    // delete review
    @Delete
    suspend fun deleteReview(review:Review)
}