package ru.mvrlrd.playlistmaker.mediateka.playlists.data

import android.content.Context
import android.os.Environment
import android.util.Log
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.InternalStorageRepository
import java.io.File

class InternalStorageRepositoryImpl(private val context: Context): InternalStorageRepository {
    override fun getFile(imageName: String, albumName: String): File? {
        return try {
            val filePath = File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                albumName
            )
            File(filePath, imageName)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

   override fun getFileWhereToSavePicture(imageName: String, albumName: String): File {
        val filePath =
            File(
                context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                albumName
            )
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        return File(filePath, imageName)
    }


    private fun log(text: String){
        Log.d(TAG, text)
    }

    companion object{
        private const val TAG = "InternalStorageRepositoryImpl"
    }

}



