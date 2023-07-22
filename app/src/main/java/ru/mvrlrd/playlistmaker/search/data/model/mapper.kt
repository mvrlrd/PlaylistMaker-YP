package ru.mvrlrd.playlistmaker.search.data.model

import ru.mvrlrd.playlistmaker.player.domain.PlayerTrack
import ru.mvrlrd.playlistmaker.search.data.storage.TrackToStorage
import ru.mvrlrd.playlistmaker.search.domain.TrackForAdapter

fun TrackDto.mapToTrack(isFavorite: Boolean) = TrackForAdapter(
    trackId,
    trackName,
    artistName,
    trackTime,
    image,
    album,
    year,
    genre,
    country,
    previewUrl,
    isFavorite
)


fun TrackToStorage.mapToTrack() = TrackForAdapter(
    trackId,
    trackName,
    artistName,
    trackTime,
    image,
    album,
    year,
    genre,
    country,
    previewUrl
)


fun TrackForAdapter.mapToTrackToStorage() = TrackForAdapter(
    trackId,
    trackName,
    artistName,
    trackTime,
    image,
    album,
    year,
    genre,
    country,
    previewUrl
)


fun TrackForAdapter.mapTrackToTrackForPlayer() = PlayerTrack(
    trackId,
    trackName,
    artistName,
    trackTime,
    image,
    album,
    year,
    genre,
    country,
    previewUrl,
    isFavorite
)