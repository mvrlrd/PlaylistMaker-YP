package ru.mvrlrd.playlistmaker.mediateka.playlists

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.mvrlrd.playlistmaker.R
import ru.mvrlrd.playlistmaker.mediateka.playlists.add_playlist_screen.ui.AddPlaylistScreenState
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.GetInternalFileUseCase
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.PlaylistForAdapter
import ru.mvrlrd.playlistmaker.tools.generateImageNameForStorage
import java.io.File
import java.io.FileOutputStream

abstract class HandlePlaylistBaseViewModel(private val useCase: GetInternalFileUseCase): ViewModel() {
    private val _screenState = MutableLiveData<AddPlaylistScreenState>()
    val screenState: LiveData<AddPlaylistScreenState> = _screenState


    abstract fun handlePlaylist(playlist: PlaylistForAdapter)

    fun changeSubmitButtonStatus(text: String?){
        _screenState.value = AddPlaylistScreenState.CreateButtonHandler(text.isNullOrEmpty())
    }

    open fun getImagePath(isPictureChosen: Boolean, path:String?=null): String{
        return if (isPictureChosen){
            generateImageNameForStorage()
        }else{
            ""
        }
    }

    fun saveImageToInternalStorage(uri: Uri?, imageName: String, albumName: String){
        useCase.saveImageToInternalStorage(uri, imageName, albumName)
    }

    fun getFile(imageName: String, albumName: String): File?{
        return useCase.getInternalFile(imageName, albumName)
    }


     fun ifDataUnsaved(name: String?, description: String?, isPictureChosen: Boolean ): Boolean {
        return ( isPictureChosen
                || !name.isNullOrEmpty()
                || !description.isNullOrEmpty())
    }



   abstract fun createPlaylist(id: Long, name: String, description: String, imageUrl: String): PlaylistForAdapter
}
