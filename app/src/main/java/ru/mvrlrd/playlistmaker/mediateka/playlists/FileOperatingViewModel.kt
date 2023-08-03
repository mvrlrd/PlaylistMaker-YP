package ru.mvrlrd.playlistmaker.mediateka.playlists


import androidx.lifecycle.ViewModel
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.GetInternalFileUseCase
import java.io.File

abstract class FileOperatingViewModel(private val useCase: GetInternalFileUseCase): ViewModel(),
    FileManipulator {
    override fun getFile(imageName: String, albumName: String): File?{
        return useCase.getInternalFile(imageName, albumName)
    }
    fun getFileToSaveImage(imageName: String, albumName: String): File{
        return useCase.getFileWhereToSavePicture(imageName, albumName)
    }
}