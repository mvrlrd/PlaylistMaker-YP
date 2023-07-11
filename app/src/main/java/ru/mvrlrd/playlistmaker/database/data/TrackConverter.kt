package ru.mvrlrd.playlistmaker.database.data
import android.os.SystemClock
import ru.mvrlrd.playlistmaker.player.domain.PlayerTrack
import ru.mvrlrd.playlistmaker.search.domain.AdapterTrack
import java.util.Calendar

class TrackConverter {
    fun mapAdapterTrackToEntity(adapterTrack: AdapterTrack) =
        TrackEntity(
            id = adapterTrack.trackId,
            trackName = adapterTrack.trackName,
            artistName = adapterTrack.artistName,
            trackTime = adapterTrack.trackTime,
            image = adapterTrack.image,
            album = adapterTrack.album,
            year = adapterTrack.year,
            genre = adapterTrack.genre,
            country = adapterTrack.country,
            previewUrl = adapterTrack.previewUrl,
            date = Calendar.getInstance().timeInMillis
        )

    fun mapPlayerTrackToEntity(track: PlayerTrack) =
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
            date = Calendar.getInstance().timeInMillis
        )

    fun mapEntityToPlayerTrack(track: TrackEntity) =
        PlayerTrack(
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

    fun mapEntityToAdapterTrack(track: TrackEntity) =
        AdapterTrack(
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