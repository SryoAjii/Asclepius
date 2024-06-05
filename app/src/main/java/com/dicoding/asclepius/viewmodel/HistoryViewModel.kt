package com.dicoding.asclepius.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.DatabaseRepository.HistoryRepository
import com.dicoding.asclepius.database.AnalyzeHistory

class HistoryViewModel(application: Application) : ViewModel() {
    private val mHistoryRepository: HistoryRepository = HistoryRepository(application)

    fun getAllHistory(): LiveData<List<AnalyzeHistory>> = mHistoryRepository.getAllHistory()

    fun delete(analyzeHistory: AnalyzeHistory) {
        mHistoryRepository.delete(analyzeHistory)
    }
}