package com.example.flowershopapp.Entities

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.imageResource
import androidx.room.TypeConverter
import java.io.ByteArrayOutputStream
import java.io.File
import java.sql.Blob

class Converters {
    @TypeConverter
    fun fromByteArray(byteArray: ByteArray): String {
        return byteArray.toString(Charsets.ISO_8859_1)
    }

    @TypeConverter
    fun toByteArray(value: String): ByteArray {
        return value.toByteArray(Charsets.ISO_8859_1)
    }
    fun imageResourceToByteArray(context: Context, resId: Int): ByteArray {
        val bitmap = BitmapFactory.decodeResource(context.resources, resId)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
    fun bitmapToByteArray(bitmap: Bitmap): ByteArray {
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream)
        return stream.toByteArray()
    }
    fun resizeByteArrayImage(imageData: ByteArray, reqWidth: Int, reqHeight: Int): ByteArray {
        val originalBitmap = BitmapFactory.decodeByteArray(imageData, 0, imageData.size)

        val resizedBitmap = Bitmap.createScaledBitmap(originalBitmap, reqWidth, reqHeight, true)

        val outputStream = ByteArrayOutputStream()
        resizedBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
        return outputStream.toByteArray()
    }
}