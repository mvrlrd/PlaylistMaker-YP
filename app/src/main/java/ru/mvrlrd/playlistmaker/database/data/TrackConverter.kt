package ru.mvrlrd.playlistmaker.database.data
import ru.mvrlrd.playlistmaker.player.domain.PlayerTrack
import ru.mvrlrd.playlistmaker.search.domain.TrackForAdapter
import java.util.Calendar

class TrackConverter {

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

    fun mapEntityToAdapterTrack(track: TrackEntity) =
        TrackForAdapter(
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