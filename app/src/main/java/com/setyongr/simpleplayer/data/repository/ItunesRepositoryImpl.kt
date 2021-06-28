package com.setyongr.simpleplayer.data.repository

import com.setyongr.simpleplayer.data.ItunesService
import com.setyongr.simpleplayer.data.model.ItunesResponse
import javax.inject.Inject

class ItunesRepositoryImpl @Inject constructor(
    private val itunesService: ItunesService
) : ItunesRepository {
    override suspend fun search(term: String, media: String): ItunesResponse {
        return itunesService.search(term, media)
    }
}