package ru.mvrlrd.playlistmaker.mediateka.playlists.domain

import java.io.File

class GetInternalFileUseCaseImpl(private val repository: InternalStorageRepository): GetInternalFileUseCase {
    override fun getInternalFile(imageName: String, albumName: String): File? {
        return repository.getFile(imageName, albumName)
    }

    override fun getFileWhereToSavePicture(imageName: String, albumName: String): File {
        return repository.getFileWhereToSavePicture(imageName, albumName)
    }
}