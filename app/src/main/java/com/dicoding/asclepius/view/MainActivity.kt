package com.dicoding.asclepius.view

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.dicoding.asclepius.DatabaseRepository.DateHelper
import com.dicoding.asclepius.R
import com.dicoding.asclepius.database.AnalyzeHistory
import com.dicoding.asclepius.databinding.ActivityMainBinding
import com.dicoding.asclepius.helper.ImageClassifierHelper
import com.dicoding.asclepius.viewmodel.HistoryAddViewModel
import com.dicoding.asclepius.viewmodel.ViewModelFactory
import org.tensorflow.lite.task.vision.classifier.Classifications
import java.text.NumberFormat

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var imageClassifierHelper: ImageClassifierHelper

    private var currentImageUri: Uri? = null

    private lateinit var displayResults : String

    private lateinit var historyAddViewModel: HistoryAddViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        historyAddViewModel = getViewModel(this@MainActivity)

        binding.galleryButton.setOnClickListener { startGallery() }
        binding.newsButton.setOnClickListener { moveToNews() }
        binding.analyzeButton.setOnClickListener {
            currentImageUri?.let {
                analyzeImage()
                moveToResult()
            } ?:run {
                showToast(getString(R.string.no_img_warning))
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.history_menu -> {
                val historyIntent = Intent(this@MainActivity, HistoryActivity::class.java)
                startActivity(historyIntent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun startGallery() {
        // TODO: Mendapatkan gambar dari Gallery.
        galleryLauncher.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
    }

    private val galleryLauncher = registerForActivityResult(
        ActivityResultContracts.PickVisualMedia()
    ) {  uri: Uri? ->
        if (uri != null) {
            currentImageUri = uri
            showImage()
        } else {
            Log.d("Photo Picker", "No Media Selected")
        }
    }

    private fun showImage() {
        // TODO: Menampilkan gambar sesuai Gallery yang dipilih.
        currentImageUri?.let {
            Log.d("Image URI", "showImage: $it")
            binding.previewImageView.setImageURI(it)
        }
    }

    private fun analyzeImage() {
        // TODO: Menganalisa gambar yang berhasil ditampilkan.
        imageClassifierHelper = ImageClassifierHelper(
            context = this,
            classifierListener = object : ImageClassifierHelper.ClassifierListener {
                override fun onError(error: String) {
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, error, Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onResult(result: List<Classifications>?, timeInference: Long) {
                    runOnUiThread {
                        result?.let { it ->
                            if (it.isNotEmpty() && it[0].categories.isNotEmpty()) {
                                println(it)
                                val classifyResult = it[0].categories[0]
                                val display = classifyResult.label + " " + NumberFormat.getPercentInstance().format(classifyResult.score).trim()
                                displayResults = display
                                val date = DateHelper.getDate()
                                val history = AnalyzeHistory(imageUri = currentImageUri.toString(), result = display, date = date)
                                historyAddViewModel.insert(history)
                            } else {
                                displayResults = ""
                            }
                        }
                    }
                }
            }
        )
        currentImageUri?.let { uri ->
            imageClassifierHelper.classifyStaticImage(uri)
        }

    }

    private fun moveToResult() {
        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("EXTRA_IMAGE_URI", currentImageUri.toString())
        intent.putExtra("EXTRA_RESULT", displayResults)
        startActivity(intent)
    }

    private fun moveToNews() {
        val intent = Intent(this, NewsActivity::class.java)
        startActivity(intent)
    }

    private fun getViewModel(activity: AppCompatActivity): HistoryAddViewModel {
        val modelFactory = ViewModelFactory.getInstance(activity.application)
        return ViewModelProvider(activity, modelFactory)[HistoryAddViewModel::class.java]
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}