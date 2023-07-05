package ru.mvrlrd.playlistmaker.favorites.data
import android.os.SystemClock
import ru.mvrlrd.playlistmaker.player.domain.TrackForPlayer
import ru.mvrlrd.playlistmaker.search.domain.Track
import java.sql.Date
import java.util.Calendar

class TrackConverter {
    fun mapToEntity(track: Track) =
        TrackEntity(
            id = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTime = track.trackTime,
            image = track.image,
            album = track.album,
            year = track.year,
            genre = track.genre,
            country = track.country,
            previewUrl = track.previewUrl,
            date = SystemClock.currentThreadTimeMillis()
        )

    fun mapToEntity(track: TrackForPlayer) =
        TrackEntity(
            id = track.trackId,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTime = track.trackTime,
            image = track.image,
            album = track.album,
            year = track.year,
            genre = track.genre,
            country = track.country,
            previewUrl = track.previewUrl,
            date = SystemClock.currentThreadTimeMillis()
        )

    fun mapToPlayer(track: TrackEntity) =
        TrackForPlayer(
            trackId = track.id,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTime = track.trackTime,
            image = track.image,
            album = track.album,
            year = track.year,
            genre = track.genre,
            country = track.country,
            previewUrl = track.previewUrl
        )

    fun mapToAdapter(track: TrackEntity) =
        Track(
            trackId = track.id,
            trackName = track.trackName,
            artistName = track.artistName,
            trackTime = track.trackTime,
            image = track.image,
            album = track.album,
            year = track.year,
            genre = track.genre,
            country = track.country,
            previewUrl = track.previewUrl
        )
}