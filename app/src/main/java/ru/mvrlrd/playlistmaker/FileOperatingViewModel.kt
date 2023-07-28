package ru.mvrlrd.playlistmaker

import android.net.Uri
import androidx.lifecycle.ViewModel
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.GetInternalFileUseCase
import java.io.File

abstract class FileOperatingViewModel(private val useCase: GetInternalFileUseCase): ViewModel(), FileManipulator {
    override fun getFile(imageName: String, albumName: String): File?{
        return useCase.getInternalFile(imageName, albumName)
    }
    fun saveImageToInternalStorage(uri: Uri?, imageName: String, albumName: String){
        useCase.saveImageToInternalStorage(uri, imageName, albumName)
    }
}