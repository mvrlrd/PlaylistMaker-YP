package ru.mvrlrd.playlistmaker.search.data.model

import ru.mvrlrd.playlistmaker.player.domain.PlayerTrack
import ru.mvrlrd.playlistmaker.search.data.storage.TrackToStorage
import ru.mvrlrd.playlistmaker.search.domain.AdapterTrack

fun TrackDto.mapToTrack(isFavorite:Boolean) = AdapterTrack(trackId, trackName, artistName, trackTime, image, album, year, genre, country, previewUrl, isFavorite)


fun TrackToStorage.mapToTrack() = AdapterTrack(trackId, trackName, artistName, trackTime, image, album, year, genre, country, previewUrl)


fun AdapterTrack.mapToTrackToStorage() = AdapterTrack(trackId, trackName, artistName, trackTime, image, album, year, genre, country, previewUrl)


fun AdapterTrack.mapTrackToTrackForPlayer() = PlayerTrack(trackId, trackName, artistName, trackTime, image, album, year, genre, country, previewUrl, isFavorite)