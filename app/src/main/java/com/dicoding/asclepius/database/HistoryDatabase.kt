package com.dicoding.asclepius.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [AnalyzeHistory::class], version = 1)
abstract class HistoryDatabase: RoomDatabase() {
    abstract fun analyzeHistoryDao(): AnalyzeHistoryDao

    companion object {
        @Volatile
        private var INSTANCE: HistoryDatabase? = null

        @JvmStatic
        fun getHistoryDatabase(context: Context): HistoryDatabase{
            if (INSTANCE == null) {
                synchronized(HistoryDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext, HistoryDatabase::class.java, "history_database")
                        .build()
                }
            }
            return INSTANCE as HistoryDatabase
        }
    }
}