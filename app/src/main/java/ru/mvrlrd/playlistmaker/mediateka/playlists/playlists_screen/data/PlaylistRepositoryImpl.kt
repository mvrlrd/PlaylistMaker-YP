package ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_screen.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.PlaylistForAdapter
import ru.mvrlrd.playlistmaker.mediateka.playlists.playlists_screen.domain.PlaylistRepository
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.PlaylistConverter
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.PlaylistDb

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
