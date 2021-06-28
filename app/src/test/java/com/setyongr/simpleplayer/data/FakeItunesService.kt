package com.setyongr.simpleplayer.data

import com.setyongr.simpleplayer.data.model.ItunesResponse

open class FakeItunesService : ItunesService {
    override suspend fun search(term: String, media: String): ItunesResponse {
        TODO("Not yet implemented")
    }
}