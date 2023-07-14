package ru.mvrlrd.playlistmaker.playlistDb.data

import ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.domain.AdapterPlaylist
import ru.mvrlrd.playlistmaker.playlistDb.PlaylistEntity

class PlaylistConverter {
    fun mapEntityToAdapter(playlistEntity: PlaylistEntity): AdapterPlaylist{
        with(playlistEntity){
            return AdapterPlaylist(playlistId, name, description, playlistImagePath)
        }
    }

    fun mapAdapterToEntity(playlist: AdapterPlaylist): PlaylistEntity{
        with(playlist){
            return PlaylistEntity(playlistId = 0, name =  name, description =  description, playlistImagePath =  playlistImagePath)
        }
    }

    fun convertEntityListToAdapterList(entityList: List<PlaylistEntity>): List<AdapterPlaylist>{
        return entityList.map { mapEntityToAdapter(it) }
    }

    fun convertAdapterListToEntityList(adapterList: List<AdapterPlaylist>): List<PlaylistEntity>{
       return adapterList.map { mapAdapterToEntity(it) }
    }
}