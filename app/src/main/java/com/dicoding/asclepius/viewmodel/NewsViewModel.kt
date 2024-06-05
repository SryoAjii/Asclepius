package com.dicoding.asclepius.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.asclepius.BuildConfig
import com.dicoding.asclepius.api.response.ArticlesItem
import com.dicoding.asclepius.api.response.HealthNewsResponse
import com.dicoding.asclepius.api.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NewsViewModel : ViewModel() {

    private val _newsItem = MutableLiveData<List<ArticlesItem>>()
    val newsItem: LiveData<List<ArticlesItem>> = _newsItem

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    init {
        getNews(BuildConfig.API_KEY)
    }

    fun getNews (news: String) {
        _isLoading.value = true
        val client = ApiConfig.getServiceApi().getApiKey(news)
        client.enqueue(object: Callback<HealthNewsResponse> {
            override fun onResponse(
                call: Call<HealthNewsResponse>,
                response: Response<HealthNewsResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val newsItem = response.body()?.articles
                    val filteredNewsItem = newsItem?.filter { it.title != "[Removed]" }
                    _newsItem.value = filteredNewsItem?: emptyList()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<HealthNewsResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }

    companion object {
        private const val TAG = "NewsActivity"
    }
}