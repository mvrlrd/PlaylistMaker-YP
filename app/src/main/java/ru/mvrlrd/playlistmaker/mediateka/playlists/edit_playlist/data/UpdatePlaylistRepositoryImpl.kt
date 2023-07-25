package ru.mvrlrd.playlistmaker.mediateka.playlists.edit_playlist.data

import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.PlaylistConverter
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.PlaylistDb
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.PlaylistForAdapter
import ru.mvrlrd.playlistmaker.mediateka.playlists.edit_playlist.domain.UpdatePlaylistRepository

class UpdatePlaylistRepositoryImpl(private val database: PlaylistDb, private val converter: PlaylistConverter): UpdatePlaylistRepository {
    override suspend fun updatePlaylist(playlist: PlaylistForAdapter) {
        database.getDao().updatePlaylist(converter.mapAdapterToEntity(playlist))
    }
}