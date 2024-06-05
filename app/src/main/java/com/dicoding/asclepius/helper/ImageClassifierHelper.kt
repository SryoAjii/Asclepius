package com.dicoding.asclepius.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.SystemClock
import android.provider.MediaStore
import android.util.Log
import com.dicoding.asclepius.R
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.CastOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.task.core.BaseOptions
import org.tensorflow.lite.task.vision.classifier.Classifications
import org.tensorflow.lite.task.vision.classifier.ImageClassifier
import java.lang.IllegalStateException


class ImageClassifierHelper(
    var threshold: Float = 0.5f,
    var maxResults: Int = 1,
    val mName: String = "cancer_classification.tflite",
    val context: Context,
    val classifierListener: ClassifierListener?
) {

    private var imageClassifier: ImageClassifier? = null

    init {
        setupImageClassifier()
    }

    private fun setupImageClassifier() {
        val builderOptions = ImageClassifier.ImageClassifierOptions.builder()
            .setScoreThreshold(threshold)
            .setMaxResults(maxResults)
        val baseBuilderOptions = BaseOptions.builder()
            .setNumThreads(4)
        builderOptions.setBaseOptions(baseBuilderOptions.build())

        try {
            imageClassifier = ImageClassifier.createFromFileAndOptions(
                context,
                mName,
                builderOptions.build()
            )
        } catch (e: IllegalStateException) {
            classifierListener?.onError(context.getString(R.string.image_process_failed))
            Log.e(TAG, e.message.toString())
        }
    }

    fun classifyStaticImage(imageUri: Uri) {
        if (imageClassifier == null) {
            setupImageClassifier()
        }

        val processImage = ImageProcessor.Builder()
            .add(ResizeOp(224,224, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
            .add(CastOp(DataType.UINT8))
            .build()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val source = ImageDecoder.createSource(context.contentResolver, imageUri)
            ImageDecoder.decodeBitmap(source)
        } else {
            MediaStore.Images.Media.getBitmap(context.contentResolver, imageUri)
        }.copy(Bitmap.Config.ARGB_8888, true)?.let { bitmap ->
            val tensorImg = processImage.process(TensorImage.fromBitmap(bitmap))

            var timeInference = SystemClock.uptimeMillis()
            val result = imageClassifier?.classify(tensorImg)
            timeInference = SystemClock.uptimeMillis() - timeInference
            classifierListener?.onResult(
                result,
                timeInference
            )
        }
    }


    interface ClassifierListener {
        fun onError(error: String)
        fun onResult(
            result:List<Classifications>?,
            timeInference: Long
        )
    }

    companion object {
        private const val TAG = "ImageClassifierHelper"
    }

}


