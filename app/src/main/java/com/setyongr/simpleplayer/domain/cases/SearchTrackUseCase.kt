package com.setyongr.simpleplayer.domain.cases

import com.setyongr.simpleplayer.core.data.Load
import com.setyongr.simpleplayer.core.data.tryNetwork
import com.setyongr.simpleplayer.data.repository.ItunesRepository
import com.setyongr.simpleplayer.domain.mapper.mapToTrackResult
import com.setyongr.simpleplayer.domain.model.TrackResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchTrackUseCase @Inject constructor(
    private val itunesRepository: ItunesRepository
) {
    operator fun invoke(term: String): Flow<Load<List<TrackResult>>> {
        return flow {
            emit(Load.Loading)
            tryNetwork(
                block = {
                    val data = itunesRepository.search(term, MUSIC_MEDIA).mapToTrackResult()
                    emit(Load.Success(data))
                },
                onNetworkException = {
                    emit(Load.Fail(it))
                }
            )
        }
    }

    companion object {
        const val MUSIC_MEDIA = "music"
    }
}