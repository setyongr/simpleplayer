package com.setyongr.simpleplayer.data

import com.setyongr.simpleplayer.data.model.ItunesResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesService {
    @GET("search")
    suspend fun search(
        @Query("term") term: String,
        @Query("media") media: String
    ): ItunesResponse
}