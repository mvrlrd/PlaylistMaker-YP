package ru.mvrlrd.playlistmaker.domain

import ru.mvrlrd.playlistmaker.data.model.TrackModel

fun TrackModel.mapTrackModelToTrack(): Track {
    return Track(
        trackId = this.trackId,
        trackName = this.trackName,
        artistName = this.artistName,
        trackTime = this.trackTime,
        image = this.image,
        album = this.album,
        year = this.year,
        genre = this.genre,
        country = this.country,
        previewUrl = this.previewUrl
    )
}