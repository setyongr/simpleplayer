package com.setyongr.simpleplayer.domain.cases

import com.setyongr.simpleplayer.data.model.ItunesResponse
import com.setyongr.simpleplayer.data.repository.ItunesRepository
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test

class SearchTrackUseCaseTest {
    private var searchTerm: String = ""
    private var searchMedia: String = ""
    private val fakeRepository = object : ItunesRepository {
        override suspend fun search(term: String, media: String): ItunesResponse {
            searchTerm = term
            searchMedia = media
            return ItunesResponse(0, emptyList())
        }
    }

    @Test
    fun `should fetch and return search correctly`() {
        val term = "example term"
        val uc = SearchTrackUseCase(fakeRepository)

        runBlockingTest { uc.invoke(term).collect() }

        Assert.assertEquals(term, searchTerm)
        Assert.assertEquals(SearchTrackUseCase.MUSIC_MEDIA, searchMedia)
    }
}