package com.dicoding.asclepius.DatabaseRepository


import androidx.recyclerview.widget.DiffUtil
import com.dicoding.asclepius.database.AnalyzeHistory

class DiffCallback(private val oldHistoryList: List<AnalyzeHistory>, private val newHistoryList: List<AnalyzeHistory>): DiffUtil.Callback() {
    override fun getOldListSize(): Int = oldHistoryList.size
    override fun getNewListSize(): Int = newHistoryList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldHistoryList[oldItemPosition].id == newHistoryList[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldHistory = oldHistoryList[oldItemPosition]
        val newHistory = newHistoryList[newItemPosition]
        return oldHistory.imageUri == newHistory.imageUri && oldHistory.result == newHistory.result
    }
}