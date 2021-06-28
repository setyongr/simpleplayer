package com.setyongr.simpleplayer.data

import com.setyongr.simpleplayer.data.model.ItunesResponse
import com.setyongr.simpleplayer.data.repository.ItunesRepositoryImpl
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class ItunesRepositoryImplTest {
    private var searchTerm: String = ""
    private var searchMedia: String = ""
    private val fakeService: ItunesService = object : FakeItunesService() {
        override suspend fun search(term: String, media: String): ItunesResponse {
            searchTerm = term
            searchMedia = media
            return ItunesResponse(0, emptyList())
        }
    }

    @Test
    fun `search should return correct result`() {
        val term = "example search"
        val media = "example media"
        val repo = ItunesRepositoryImpl(fakeService)

        runBlocking { repo.search(term, media) }

        Assert.assertEquals(term, searchTerm)
        Assert.assertEquals(media, searchMedia)
    }
}