package com.example.photogallery

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photogallery.flickr.FlickrRetrofit
import com.example.photogallery.flickr.Photo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PhotoViewModel : ViewModel() {
    private val _photos = MutableStateFlow<List<Photo>>(emptyList())
    val photos = _photos.asStateFlow()
    private var originalPhotos: List<Photo> = emptyList()

    init {
        loadInteresting()
    }

    fun loadInteresting() {
        viewModelScope.launch {
            try {
                val response = FlickrRetrofit.api.getInterestingPhotos()
                val list = response.photos.photo.filter { it.url != null }
                originalPhotos = list
                _photos.value = list
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun search(query: String) {
        val filtered = if (query.isBlank()) {
            originalPhotos
        } else {
            originalPhotos.filter { it.title.contains(query, ignoreCase = true) }
        }
        _photos.value = filtered
    }
    fun reload() {
        _photos.value = originalPhotos
    }
}