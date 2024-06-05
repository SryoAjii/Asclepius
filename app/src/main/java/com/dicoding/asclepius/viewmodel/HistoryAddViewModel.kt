package com.dicoding.asclepius.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.DatabaseRepository.HistoryRepository
import com.dicoding.asclepius.database.AnalyzeHistory

class HistoryAddViewModel(application: Application) : ViewModel() {
    private val mHistoryRepository: HistoryRepository = HistoryRepository(application)

    fun insert(analyzeHistory: AnalyzeHistory) {
        mHistoryRepository.insert(analyzeHistory)
    }
}