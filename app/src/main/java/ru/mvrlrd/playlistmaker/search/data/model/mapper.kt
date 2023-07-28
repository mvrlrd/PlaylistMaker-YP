package ru.mvrlrd.playlistmaker.search.data.model

import ru.mvrlrd.playlistmaker.player.domain.PlayerTrack
import ru.mvrlrd.playlistmaker.search.data.storage.TrackToStorage
import ru.mvrlrd.playlistmaker.search.domain.TrackForAdapter

fun TrackDto.mapDtoToTrack() = TrackForAdapter(
    trackId,
    trackName,
    artistName,
    trackTime,
    image,
    image60,
    album,
    year,
    genre,
    country,
    previewUrl,
    isFavorite = false
)


fun TrackToStorage.mapDtoToTrack() = TrackForAdapter(
    trackId,
    trackName,
    artistName,
    trackTime,
    image,
    image60,
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
    image60,
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
    image60,
    album,
    year,
    genre,
    country,
    previewUrl,
    isFavorite
)