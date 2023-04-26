package ru.mvrlrd.playlistmaker.search.data.model

import ru.mvrlrd.playlistmaker.search.data.storage.TrackToStorage
import ru.mvrlrd.playlistmaker.search.domain.Track

fun TrackDto.mapToTrack(): Track {
    return Track(trackId, trackName, artistName, trackTime, image, album, year, genre, country, previewUrl)
}

fun TrackToStorage.mapToTrack(): Track {
    return Track(trackId, trackName, artistName, trackTime, image, album, year, genre, country, previewUrl)
}

fun Track.mapToTrackToStorage(): Track {
    return Track(trackId, trackName, artistName, trackTime, image, album, year, genre, country, previewUrl)
}