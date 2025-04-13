package esi.roadside.assistance.client.auth.domain.repository

import android.net.Uri
import com.cloudinary.android.callback.ErrorInfo

interface CloudinaryRepo {
    fun uploadImage(
        image: Uri,
        onSuccess: (String) -> Unit,
        onProgress: (Float) -> Unit,
        onFailure: (ErrorInfo?) -> Unit,
        onFinished: () -> Unit,
    )
}