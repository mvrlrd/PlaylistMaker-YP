package ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db

import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.PlaylistForAdapter
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.entities.PlaylistEntity
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.entities.Song
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.relations.PlaylistWithSongs
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlist_info_screen.domain.PlaylistInfo
import ru.mvrlrd.playlistmaker.search.domain.TrackForAdapter

class PlaylistConverter {
     fun mapEntityToAdapter(playlistEntity: PlaylistEntity): PlaylistForAdapter {
        with(playlistEntity) {
            return PlaylistForAdapter(playlistId, name, description, playlistImagePath)
        }
    }

    fun mapAdapterToEntity(playlist: PlaylistForAdapter): PlaylistEntity {
        with(playlist) {
            return PlaylistEntity(
                playlistId = playlistId?:0,
                name = name,
                description = description,
                playlistImagePath = playlistImagePath
            )
        }
    }

    fun convertEntityListToAdapterList(entityList: List<PlaylistEntity>): List<PlaylistForAdapter> {
        return entityList.map { mapEntityToAdapter(it) }
    }

     fun mapListDaoToListForAdapter(daoList: List<PlaylistWithSongs>): List<PlaylistForAdapter> {
        return daoList.map {
            PlaylistForAdapter(
                playlistId = it.playlist.playlistId,
                name = it.playlist.name,
                description = it.playlist.description,
                playlistImagePath = it.playlist.playlistImagePath,
                tracksQuantity = it.songs.size
            )
        }
    }

     fun mapSongsToTracks(songs: List<Song>): List<TrackForAdapter>{
        return songs.map {
            TrackForAdapter(
                trackId = it.songId,
                trackName = it.trackName,
                artistName = it.artistName,
                trackTime = it.trackTime,
                image = it.image,
                image60 = it.image60,
                album = it.album,
                year = it.year,
                genre = it.genre,
                country = it.country,
                previewUrl = it.previewUrl,
                isFavorite = false
            )
        }
    }

     fun mapPlaylistWithSongsToPlaylistInfo(playlist: PlaylistWithSongs): PlaylistInfo {
        return PlaylistInfo(
            playlist = mapEntityToAdapter(playlist.playlist),
            songs = mapSongsToTracks(playlist.songs)
        )
    }



}