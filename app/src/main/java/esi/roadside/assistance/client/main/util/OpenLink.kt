package esi.roadside.assistance.client.main.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.net.toUri

fun Context.launch(intent: Intent, alternate: Intent? = null) {
    startActivity(
        intent.takeIf { intent.resolveActivity(packageManager) != null } ?: alternate ?: return
    )
}

fun Context.openLink(link: String, alternate: String? = null) {
    launch(
        Intent(Intent.ACTION_VIEW, link.toUri()),
        Intent(Intent.ACTION_VIEW, alternate?.toUri())
    )
}