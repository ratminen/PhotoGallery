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

    init {
        loadPhotos()
    }

    private fun loadPhotos() {
        viewModelScope.launch {
            try {
                val response = FlickrRetrofit.api.getInterestingPhotos()
                _photos.value = response.photos.photo.filter { it.url != null }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}