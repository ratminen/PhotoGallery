package com.example.photogallery

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.photogallery.flickr.FlickrRetrofit
import com.example.photogallery.flickr.Photo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import com.example.photogallery.room.AppDatabase
import com.example.photogallery.room.FavoritePhoto
import com.example.photogallery.room.PhotoRepository

class PhotoViewModel(application: Application) :  AndroidViewModel(application) {
    private val repository: PhotoRepository
    val allFavorites: LiveData<List<FavoritePhoto>>
    val searchResults: MutableLiveData<List<Photo>>
    private var originalPhotos: List<Photo> = emptyList()

    init {
        val db = AppDatabase.getDatabase(application)
        val dao = db.favoritePhotoDao()
        repository = PhotoRepository(dao)
        allFavorites= repository.allFavorites
        searchResults= repository.searchResults
        loadInteresting()
    }

    fun loadInteresting() {
        viewModelScope.launch {
            try {
                val response = FlickrRetrofit.api.getInterestingPhotos()
                val list = response.photos.photo.filter { it.url != null }
                originalPhotos = list
                searchResults.postValue(list)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
    fun search(query: String) {
        val filtered = if (query.isBlank()) originalPhotos
        else originalPhotos.filter { it.title.contains(query, ignoreCase = true) }
        searchResults.postValue(filtered)
    }
    fun reload() {
        loadInteresting()
    }

    fun addToFavorites(photo: Photo) {
        repository.addToFavorites(photo)
    }

    fun clearFavorites() {
        repository.deleteAllFavorites()
    }
}