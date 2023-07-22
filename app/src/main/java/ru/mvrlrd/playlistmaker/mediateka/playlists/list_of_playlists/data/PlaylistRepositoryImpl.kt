package ru.mvrlrd.playlistmaker.mediateka.playlists.list_of_playlists.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.mvrlrd.playlistmaker.mediateka.playlists.list_of_playlists.domain.PlaylistForAdapter
import ru.mvrlrd.playlistmaker.mediateka.playlists.list_of_playlists.domain.PlaylistRepository
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.PlaylistConverter
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.PlaylistDb
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.relations.PlaylistWithSongs

class PlaylistRepositoryImpl(
    private val dataBase: PlaylistDb,
    private val converter: PlaylistConverter
) : PlaylistRepository {

    override fun getAllPlaylistsWithSongs(): Flow<List<PlaylistForAdapter>> {
        return dataBase.getDao().getPlaylistsWithSongs().map {
            converter.mapListDaoToListForAdapter(it)
        }
    }
}
