package com.example.photogallery.room

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import com.example.photogallery.flickr.Photo
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PhotoRepository(private val dao: FavoritePhotoDao)  {
    val allFavorites: LiveData<List<FavoritePhoto>> = dao.getAll().asLiveData()
    val searchResults: MutableLiveData<List<Photo>> = MutableLiveData()

    fun addToFavorites(photo: Photo) {
        CoroutineScope(Dispatchers.IO).launch {
            dao.insert(FavoritePhoto(title = photo.title, url = photo.url!!))
            Log.d("PhotoViewModel", "Inserted favorite: ${photo.title}")
        }
    }

    fun deleteAllFavorites() {
        CoroutineScope(Dispatchers.IO).launch {
            dao.deleteAll()
        }
    }
}