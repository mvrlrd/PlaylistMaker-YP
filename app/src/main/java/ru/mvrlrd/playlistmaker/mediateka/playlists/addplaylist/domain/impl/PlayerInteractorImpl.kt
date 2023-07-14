package ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.domain.impl

import kotlinx.coroutines.flow.Flow
import ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.domain.AdapterPlaylist
import ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.domain.PlaylistInteractor
import ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.domain.PlaylistRepository

class PlaylistInteractorImpl(private val repository: PlaylistRepository): PlaylistInteractor {
    override fun getAllPlaylist(): Flow<List<AdapterPlaylist>> {
        return repository.getAllPlaylist()
    }

    override suspend fun addPlaylist(adapterPlaylist: AdapterPlaylist) {
        repository.insertPlaylist(adapterPlaylist)
    }
}