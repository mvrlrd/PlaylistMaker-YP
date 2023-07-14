package ru.mvrlrd.playlistmaker.playlistDb.data

import ru.mvrlrd.playlistmaker.mediateka.playlists.addplaylist.domain.PlaylistForAdapter
import ru.mvrlrd.playlistmaker.playlistDb.PlaylistEntity

class PlaylistConverter {
    fun mapEntityToAdapter(playlistEntity: PlaylistEntity): PlaylistForAdapter{
        with(playlistEntity){
            return PlaylistForAdapter(playlistId, name, description, playlistImagePath)
        }
    }

    fun mapAdapterToEntity(playlist: PlaylistForAdapter): PlaylistEntity{
        with(playlist){
            return PlaylistEntity(playlistId = 0, name =  name, description =  description, playlistImagePath =  playlistImagePath)
        }
    }

    fun convertEntityListToAdapterList(entityList: List<PlaylistEntity>): List<PlaylistForAdapter>{
        return entityList.map { mapEntityToAdapter(it) }
    }

    fun convertAdapterListToEntityList(adapterList: List<PlaylistForAdapter>): List<PlaylistEntity>{
       return adapterList.map { mapAdapterToEntity(it) }
    }
}