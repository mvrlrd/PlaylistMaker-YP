package ru.mvrlrd.playlistmaker.mediateka.playlists.add_playlist_screen.data

import ru.mvrlrd.playlistmaker.mediateka.playlists.add_playlist_screen.domain.AddPlaylistRepository
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.PlaylistConverter
import ru.mvrlrd.playlistmaker.mediateka.playlists.data.playlists_db.PlaylistDb
import ru.mvrlrd.playlistmaker.mediateka.playlists.list_of_playlists.domain.PlaylistForAdapter

class AddPlaylistRepositoryImpl(private val dataBase: PlaylistDb,
                                private val converter: PlaylistConverter
): AddPlaylistRepository {
    override suspend fun insertPlaylist(playlistForAdapter: PlaylistForAdapter) {
        dataBase.getDao().insertPlaylist(converter.mapAdapterToEntity(playlistForAdapter))
    }
}