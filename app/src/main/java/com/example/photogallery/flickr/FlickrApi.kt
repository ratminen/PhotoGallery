package com.example.photogallery.flickr

import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApi {
    @GET("services/rest/")
    suspend fun getInterestingPhotos(
        @Query("method") method: String = "flickr.interestingness.getList",
        @Query("api_key") apiKey: String = "a901bbdbf57451c7e7e940439b4ac03d",
        @Query("extras") extras: String = "url_s",
        @Query("format") format: String = "json",
        @Query("nojsoncallback") noJsonCallback: Int = 1
    ): FlickrResponse
}