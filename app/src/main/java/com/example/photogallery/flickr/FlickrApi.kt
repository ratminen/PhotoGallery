package com.example.photogallery.flickr

import retrofit2.http.GET
import retrofit2.http.Query

interface FlickrApi {
    @GET("services/rest/")
    suspend fun getInterestingPhotos(
        @Query("method") method: String = "flickr.interestingness.getList",
        @Query("api_key") apiKey: String = "1fb883b64413143eddbc6d72438c3eff",
        @Query("extras") extras: String = "url_s",
        @Query("format") format: String = "json",
        @Query("nojsoncallback") noJsonCallback: Int = 1
    ): FlickrResponse
}