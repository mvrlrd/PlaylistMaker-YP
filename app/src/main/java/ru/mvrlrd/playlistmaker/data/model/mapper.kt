package ru.mvrlrd.playlistmaker.data.model

import ru.mvrlrd.playlistmaker.data.storage.TrackToStorage
import ru.mvrlrd.playlistmaker.domain.Track

fun TrackDto.mapToTrack(): Track {
    return Track(trackId, trackName, artistName, trackTime, image, album, year, genre, country, previewUrl)
}

fun TrackToStorage.mapToTrack(): Track {
    return Track(trackId, trackName, artistName, trackTime, image, album, year, genre, country, previewUrl)
}

fun Track.mapToTrackToStorage(): Track {
    return Track(trackId, trackName, artistName, trackTime, image, album, year, genre, country, previewUrl)
}