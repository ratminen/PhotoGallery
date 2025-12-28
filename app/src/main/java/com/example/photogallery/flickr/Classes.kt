package com.example.photogallery.flickr

import com.squareup.moshi.Json

data class FlickrResponse(
    val photos: PhotosResponse
)

data class PhotosResponse(
    val photo: List<Photo>
)

data class Photo(
    val id: String,
    val title: String,

    @Json(name = "url_s")
    val url: String?
)