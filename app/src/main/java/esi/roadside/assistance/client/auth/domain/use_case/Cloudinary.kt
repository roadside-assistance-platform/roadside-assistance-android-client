package esi.roadside.assistance.client.auth.domain.use_case

import android.net.Uri
import com.cloudinary.android.callback.ErrorInfo
import esi.roadside.assistance.client.auth.domain.repository.CloudinaryRepo

class Cloudinary(private val cloudinaryRepo: CloudinaryRepo) {
    operator fun invoke(
        image: Uri,
        onSuccess: (String) -> Unit,
        onProgress: (Float) -> Unit,
        onFailure: (ErrorInfo?) -> Unit,
        onFinished: () -> Unit,
    ) = cloudinaryRepo.uploadImage(image, onSuccess, onProgress, onFailure, onFinished)
}