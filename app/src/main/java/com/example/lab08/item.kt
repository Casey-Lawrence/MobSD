package com.example.lab08
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reviews")
data class Review(@PrimaryKey(autoGenerate = true)val id: Int = 0,
    val title: String,
    val author: String,
    val genre: String,
    val review: String

)