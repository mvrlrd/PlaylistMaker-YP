package ru.mvrlrd.playlistmaker.mediateka.playlists.domain

import android.net.Uri
import java.io.File

class GetInternalFileUseCaseImpl(private val repository: InternalStorageRepository): GetInternalFileUseCase {
    override fun getInternalFile(imageName: String, albumName: String): File? {
        return repository.getFile(imageName, albumName)
    }

    override fun saveImageToInternalStorage(uri: Uri?, imageName: String, albumName: String) {
        repository.saveImageToInternalStorage(uri, imageName, albumName)
    }
}