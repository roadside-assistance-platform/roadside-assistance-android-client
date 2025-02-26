package esi.roadside.assistance.client.core.util

import android.content.Context
import android.content.pm.PackageManager
import android.os.Build

fun Context.getAppVersion(): String =
    (
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                packageManager.getPackageInfo(
                    packageName,
                    PackageManager.PackageInfoFlags.of(0),
                )
            } else {
                packageManager.getPackageInfo(packageName, 0)
            }
            ).versionName ?: "Unknown"
