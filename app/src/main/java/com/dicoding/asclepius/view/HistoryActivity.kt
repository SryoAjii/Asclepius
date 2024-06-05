package com.dicoding.asclepius.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.database.AnalyzeHistory
import com.dicoding.asclepius.databinding.ActivityHistoryBinding
import com.dicoding.asclepius.viewmodel.HistoryAdapter
import com.dicoding.asclepius.viewmodel.HistoryViewModel
import com.dicoding.asclepius.viewmodel.ViewModelFactory

class HistoryActivity : AppCompatActivity(), HistoryAdapter.OnItemClickCallback {
    private lateinit var binding: ActivityHistoryBinding
    private lateinit var adapter: HistoryAdapter
    private val historyViewModel by viewModels<HistoryViewModel>() {
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.historyList.layoutManager = layoutManager

        adapter = HistoryAdapter(this)
        historyViewModel.getAllHistory().observe(this) { history ->
            if (history != null) {
                adapter.setHistory(history)
            }
        }

        binding.historyList.setHasFixedSize(true)
        binding.historyList.adapter = adapter

    }

    override fun onItemClicked(history: AnalyzeHistory) {
        val dialogTitle = "Hapus"
        val dialogMessage = "Apakah anda ingin menghapus item ini?"
        val alertDialogBuilder = AlertDialog.Builder(this)
        with(alertDialogBuilder) {
            setTitle(dialogTitle)
            setMessage(dialogMessage)
            setCancelable(false)
            setPositiveButton("Ya") {_, _ ->
                historyViewModel.delete(history)
                showToast("Item berhasil dihapus")
            }
            setNegativeButton("Tidak") { dialog, _ ->
                dialog.cancel()
            }
            val alertDialog = alertDialogBuilder.create()
            alertDialog.show()
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}