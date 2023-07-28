package ru.mvrlrd.playlistmaker.mediateka.playlists.data

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.util.Log
import ru.mvrlrd.playlistmaker.mediateka.playlists.domain.InternalStorageRepository
import java.io.File
import java.io.FileOutputStream

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

    override fun saveImageToInternalStorage(uri: Uri?, imageName: String, albumName: String) {
        uri?.let {
            val filePath =
                File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
                    albumName)
            if (!filePath.exists()) {
                filePath.mkdirs()
            }
            val file = File(filePath, imageName)
            val inputStream = context.contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            BitmapFactory
                .decodeStream(inputStream)
                .compress(
                    Bitmap.CompressFormat.JPEG,
                    30, outputStream)
            log("image file saved to storage")
            return
        }
        log("uri == null, image wasn't saved in internal storage")
    }

    private fun log(text: String){
        Log.d(TAG, text)
    }

    companion object{
        private const val TAG = "InternalStorageRepositoryImpl"
    }

}



