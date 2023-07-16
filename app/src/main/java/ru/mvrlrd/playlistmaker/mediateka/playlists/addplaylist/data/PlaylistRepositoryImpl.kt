package ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.domain.PlaylistForAdapter
import ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.domain.PlaylistRepository
import ru.mvrlrd.playlistmaker.db_playlist.PlaylistConverter
import ru.mvrlrd.playlistmaker.db_playlist.PlaylistDb
import ru.mvrlrd.playlistmaker.db_playlist.relations.PlaylistWithSongs

class PlaylistRepositoryImpl(
    private val dataBase: PlaylistDb,
    private val converter: PlaylistConverter
) : PlaylistRepository {
    override suspend fun insertPlaylist(playlistForAdapter: PlaylistForAdapter) {
        dataBase.getDao().insertPlaylist(converter.mapAdapterToEntity(playlistForAdapter))
    }

    override fun getAllPlaylistsWithSongs(): Flow<List<PlaylistForAdapter>> {
        return dataBase.getDao().getPlaylistsWithSongs().map {
            mapListDaoToListForAdapter(it)
        }
    }

    private fun mapListDaoToListForAdapter(daoList: List<PlaylistWithSongs>): List<PlaylistForAdapter> {
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
}
