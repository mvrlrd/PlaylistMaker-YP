package ru.mvrlrd.playlistmaker.db_playlist

import ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.domain.PlaylistForAdapter
import ru.mvrlrd.playlistmaker.db_playlist.entities.PlaylistEntity

class PlaylistConverter {
    private fun mapEntityToAdapter(playlistEntity: PlaylistEntity): PlaylistForAdapter {
        with(playlistEntity) {
            return PlaylistForAdapter(playlistId, name, description, playlistImagePath)
        }
    }

    fun mapAdapterToEntity(playlist: PlaylistForAdapter): PlaylistEntity {
        with(playlist) {
            return PlaylistEntity(
                playlistId = 0,
                name = name,
                description = description,
                playlistImagePath = playlistImagePath
            )
        }
    }

    fun convertEntityListToAdapterList(entityList: List<PlaylistEntity>): List<PlaylistForAdapter> {
        return entityList.map { mapEntityToAdapter(it) }
    }
}