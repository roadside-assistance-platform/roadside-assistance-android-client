package esi.roadside.assistance.client.main.util

import android.content.Context
import android.content.Intent
import android.net.Uri

fun Context.launch(intent: Intent, alternate: Intent? = null) {
    startActivity(
        intent.takeIf { intent.resolveActivity(packageManager) != null } ?: alternate ?: return
    )
}

fun Context.openLink(link: String, alternate: String? = null) {
    launch(
        Intent(Intent.ACTION_VIEW, Uri.parse(link)),
        Intent(Intent.ACTION_VIEW, Uri.parse(alternate))
    )
}