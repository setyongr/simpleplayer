package com.setyongr.simpleplayer.data.repository

import com.setyongr.simpleplayer.data.model.ItunesResponse

interface ItunesRepository {
    /**
     * Search itunes with filter
     *
     * @param term Search term
     * @param media Media type
     *
     * @return API Responses
     */
    suspend fun search(term: String, media: String): ItunesResponse
}