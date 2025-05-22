package esi.roadside.assistance.client.settings.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log

fun Uri.toBitmap(context: Context): Bitmap? {
    return try {
        val inputStream = context.contentResolver?.openInputStream(this)
        BitmapFactory.decodeStream(inputStream)
    } catch (e: Exception) {
        Log.e("UriToBitmap", "Error converting URI to Bitmap", e)
        null
    }
}