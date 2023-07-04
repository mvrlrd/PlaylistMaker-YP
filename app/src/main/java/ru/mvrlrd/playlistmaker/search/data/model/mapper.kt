package ru.mvrlrd.playlistmaker.search.data.model

import ru.mvrlrd.playlistmaker.player.domain.TrackForPlayer
import ru.mvrlrd.playlistmaker.search.data.storage.TrackToStorage
import ru.mvrlrd.playlistmaker.search.domain.Track

fun TrackDto.mapToTrack(isFavorite:Boolean) = Track(trackId, trackName, artistName, trackTime, image, album, year, genre, country, previewUrl, isFavorite)


fun TrackToStorage.mapToTrack() = Track(trackId, trackName, artistName, trackTime, image, album, year, genre, country, previewUrl)


fun Track.mapToTrackToStorage() = Track(trackId, trackName, artistName, trackTime, image, album, year, genre, country, previewUrl)


fun Track.mapTrackToTrackForPlayer() = TrackForPlayer(trackId, trackName, artistName, trackTime, image, album, year, genre, country, previewUrl, isFavorite)