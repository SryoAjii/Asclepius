package com.dicoding.asclepius.DatabaseRepository

import android.app.Application
import androidx.lifecycle.LiveData
import com.dicoding.asclepius.database.AnalyzeHistory
import com.dicoding.asclepius.database.AnalyzeHistoryDao
import com.dicoding.asclepius.database.HistoryDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class HistoryRepository(application: Application) {
    private val mAnalyzeHistoryDao: AnalyzeHistoryDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = HistoryDatabase.getHistoryDatabase(application)
        mAnalyzeHistoryDao = db.analyzeHistoryDao()
    }

    fun getAllHistory(): LiveData<List<AnalyzeHistory>> = mAnalyzeHistoryDao.getAllHistory()

    fun insert(analyzeHistory: AnalyzeHistory) {
        executorService.execute { mAnalyzeHistoryDao.insert(analyzeHistory) }
    }

    fun delete(analyzeHistory: AnalyzeHistory) {
        executorService.execute { mAnalyzeHistoryDao.delete(analyzeHistory) }
    }
}