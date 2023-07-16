package ru.mvrlrd.playlistmaker.player.data

import android.util.Log
import androidx.lifecycle.LiveData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import ru.mvrlrd.playlistmaker.database.data.FavoriteDb
import ru.mvrlrd.playlistmaker.database.data.TrackConverter
import ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.domain.PlaylistForAdapter
import ru.mvrlrd.playlistmaker.player.domain.PlayerRepository
import ru.mvrlrd.playlistmaker.player.domain.PlayerTrack
import ru.mvrlrd.playlistmaker.db_playlist.data.PlaylistConverter
import ru.mvrlrd.playlistmaker.db_playlist.data.PlaylistDb
import ru.mvrlrd.playlistmaker.db_playlist.data.entities.PlaylistSongCrossRef
import ru.mvrlrd.playlistmaker.db_playlist.data.entities.Song
import ru.mvrlrd.playlistmaker.db_playlist.data.relations.PlaylistWithSongs

class PlayerRepositoryImpl(
    private val playerClient: PlayerClient,
    private val favoriteDb: FavoriteDb,
    private val playlistDb: PlaylistDb,
    private val playlistConverter: PlaylistConverter,
    private val trackConverter: TrackConverter
) : PlayerRepository {
    override fun preparePlayer(playerTrack: PlayerTrack) {
        playerClient.preparePlayer(playerTrack)
    }

    override fun getLivePlayerState():LiveData<MyMediaPlayer.PlayerState>{
        return playerClient.getLivePlayerState()
    }


    override fun start() {
        playerClient.start()
    }

    override fun pause() {
        playerClient.pause()
    }

    override fun onDestroy() {
        playerClient.onDestroy()
    }

    override fun getCurrentTime(): Flow<Int> {
        return playerClient.getCurrentTime()
    }

    override suspend fun addToFavorite(playerTrack: PlayerTrack) {
        favoriteDb.getDao().insertTrack(trackConverter.mapPlayerTrackToEntity(playerTrack))
    }

    override suspend fun removeFromFavorite(trackId: Int) {
        favoriteDb.getDao().deleteTrack(trackId)
    }

    override fun getAllPlaylists(): Flow<List<PlaylistForAdapter>> {
        return playlistDb.getDao().getAllPlaylists().map { list ->
            playlistConverter.convertEntityListToAdapterList(list)
        }
    }
//TODO надо получить плейлисты, по каждому плейлисту зайти в базу и получить список треков, и замапить плейлист под адаптер и впихнуть туда количество треков

    override suspend fun addTrackToPlaylist(trackId: Int, playlistId: Int) {
        playlistDb.getDao().insertTrack(Song(trackId))
        val playerSongCrossRef = PlaylistSongCrossRef(songId = trackId, playlistId = playlistId)
        playlistDb.getDao().insertPlaylistSongCrossRef(playerSongCrossRef)
        Log.e("database", "${trackId} was added to $playlistId")
    }



    override  fun getAllPlaylistsWithSongs(): Flow<List<PlaylistForAdapter>> {
        return playlistDb.getDao().getPlaylistsWithSongs().map {
            mapListDaoToListForAdapter(it)
        }
    }

    private fun mapListDaoToListForAdapter(daoList: List<PlaylistWithSongs>): List<PlaylistForAdapter>{
       return daoList.map {
            PlaylistForAdapter(
                playlistId = it.playlist.playlistId,
                name= it.playlist.name,
                description = it.playlist.description,
                playlistImagePath = it.playlist.playlistImagePath,
                tracksQuantity = it.songs.size
            )
        }
    }

}