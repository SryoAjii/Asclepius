package com.dicoding.asclepius.viewmodel

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.DatabaseRepository.DiffCallback
import com.dicoding.asclepius.database.AnalyzeHistory
import com.dicoding.asclepius.databinding.HistoryItemBinding
import com.dicoding.asclepius.view.ResultActivity

class HistoryAdapter(private var onItemClickCallback: OnItemClickCallback): RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    interface OnItemClickCallback {
        fun onItemClicked(history: AnalyzeHistory)
    }

    inner class HistoryViewHolder(private val binding: HistoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(history: AnalyzeHistory) {
            with(binding) {
                historyResult.text = history.result
                historyDate.text = history.date
                Glide.with(binding.root.context)
                    .load(Uri.parse(history.imageUri))
                    .into(binding.historyPicture)
                historyItem.setOnClickListener{
                    val intent = Intent(it.context, ResultActivity::class.java)
                    intent.putExtra("EXTRA_IMAGE_URI", history.imageUri)
                    intent.putExtra("EXTRA_RESULT", history.result)
                    it.context.startActivity(intent)
                }
            }
        }

        init {
            binding.historyDelete.setOnClickListener {
                val position: Int = adapterPosition
                val history: AnalyzeHistory = historyList[position]
                onItemClickCallback.onItemClicked(history)
            }
        }
    }



    private val historyList = ArrayList<AnalyzeHistory>()
    fun setHistory(historyList: List<AnalyzeHistory>) {
        val diffCallback = DiffCallback(this.historyList, historyList)
        val diffResult = DiffUtil.calculateDiff(diffCallback)
        this.historyList.clear()
        this.historyList.addAll(historyList)
        diffResult.dispatchUpdatesTo(this)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = HistoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(historyList[position])
    }

}