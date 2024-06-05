package com.dicoding.asclepius.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface AnalyzeHistoryDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(history: AnalyzeHistory)

    @Delete
    fun delete(history: AnalyzeHistory)

    @Query("SELECT * FROM ANALYZEHISTORY ORDER BY ID")
    fun getAllHistory(): LiveData<List<AnalyzeHistory>>

}