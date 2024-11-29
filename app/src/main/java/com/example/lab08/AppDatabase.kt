package com.example.lab08

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Review::class], version = 1, exportSchema = false) // Updated to use Review entity
abstract class AppDatabase : RoomDatabase() {

    abstract fun reviewDao(): ReviewDao // Updated to use ReviewDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                val newInstance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "novel_notes_database" // Updated database name
                )
                    .fallbackToDestructiveMigration() // Clears the database if schema changes
                    .build()
                instance = newInstance
                newInstance
            }
        }
    }
}
