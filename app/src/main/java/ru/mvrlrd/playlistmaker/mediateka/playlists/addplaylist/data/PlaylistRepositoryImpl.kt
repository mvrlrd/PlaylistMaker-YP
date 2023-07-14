package ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.domain.AdapterPlaylist
import ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.domain.PlaylistRepository
import ru.mvrlrd.playlistmaker.playlistDb.data.PlaylistConverter
import ru.mvrlrd.playlistmaker.playlistDb.data.PlaylistDb

class PlaylistRepositoryImpl(private val dataBase: PlaylistDb, private val converter: PlaylistConverter): PlaylistRepository {
    override fun getAllPlaylist(): Flow<List<AdapterPlaylist>> {
        return dataBase.getDao().getAllPlaylists().map { list ->
            converter.convertEntityListToAdapterList(list)
        }
    }

    override suspend fun insertPlaylist(adapterPlaylist: AdapterPlaylist) {
        dataBase.getDao().insertPlaylist(converter.mapAdapterToEntity(adapterPlaylist))
    }
}
