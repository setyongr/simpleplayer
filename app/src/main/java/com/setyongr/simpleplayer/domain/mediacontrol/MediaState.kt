package com.setyongr.simpleplayer.domain.mediacontrol

sealed class MediaState {
    object Play : MediaState()
    object Pause : MediaState()
    object Stop : MediaState()
}