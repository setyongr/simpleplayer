package com.setyongr.simpleplayer.data.repository

import com.setyongr.simpleplayer.data.model.ItunesResponse

interface ItunesRepository {
    suspend fun search(term: String, media: String): ItunesResponse
}