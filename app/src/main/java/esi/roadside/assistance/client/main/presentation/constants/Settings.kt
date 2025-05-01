package esi.roadside.assistance.client.main.presentation.constants

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.InsertDriveFile
import androidx.compose.material.icons.filled.Android
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.outlined.Android
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.DesignServices
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Language
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Palette
import androidx.compose.material.icons.outlined.Password
import androidx.compose.material.icons.outlined.QuestionMark
import androidx.compose.material.icons.outlined.Security
import androidx.compose.ui.graphics.vector.ImageVector
import esi.roadside.assistance.client.R
import esi.roadside.assistance.client.main.presentation.NavRoutes
import esi.roadside.assistance.client.settings.presentation.LargeSettingsGroup
import esi.roadside.assistance.client.settings.presentation.LargeSettingsItem

object Settings {
    val languages =
        mapOf(
            "system" to R.string.follow_system,
            "en" to R.string.english,
            "fr" to R.string.french,
            "ar" to R.string.arabic,
            "es" to R.string.spanish,
            "it" to R.string.italian,
            "in" to R.string.hindi,
        )

    val themeOptions =
        listOf(
            "light" to (R.string.light to Icons.Default.LightMode),
            "dark" to (R.string.dark to Icons.Default.DarkMode),
            "system" to (R.string.default_theme to Icons.Default.Android),
        )
    val colors =
        listOf(
            "blue" to R.string.blue,
            "green" to R.string.green,
            "red" to R.string.red,
            "orange" to R.string.orange,
            "purple" to R.string.purple,
        )

    val accountSettings = LargeSettingsGroup(
        R.string.account,
        listOf(
            LargeSettingsItem(
                R.string.change_password,
                Icons.Outlined.Password,
                NavRoutes.ChangePassword
            ),
            LargeSettingsItem(
                R.string.delete_account,
                Icons.Outlined.Delete,
                NavRoutes.DeleteAccount
            ),
        )
    )
    val globalSettings = LargeSettingsGroup(
        R.string.global_settings,
        listOf(
            LargeSettingsItem(
                R.string.customize_app,
                Icons.Outlined.Palette,
                NavRoutes.CustomizeApp
            ),
            LargeSettingsItem(
                R.string.language,
                Icons.Outlined.Language,
                NavRoutes.Language
            ),
            LargeSettingsItem(
                R.string.about,
                Icons.Outlined.Info,
                NavRoutes.About
            )
        )
    )
    val legalSettings = LargeSettingsGroup(
        R.string.legal,
        listOf(
            LargeSettingsItem(
                R.string.terms_of_service,
                Icons.AutoMirrored.Outlined.InsertDriveFile,
                NavRoutes.TermsOfService
            ),
            LargeSettingsItem(
                R.string.privacy_policy,
                Icons.Outlined.Security,
                NavRoutes.PrivacyPolicy
            )
        )
    )
    val supportSettings = LargeSettingsGroup(
        R.string.support,
        listOf(
            LargeSettingsItem(
                R.string.help,
                Icons.Outlined.QuestionMark,
                NavRoutes.Help
            )
        )
    )
    val groups = listOf(accountSettings, globalSettings, legalSettings, supportSettings)

    enum class DeveloperType(val label: String, val icon: ImageVector) {
        AndroidDeveloper("Android Developer", Icons.Outlined.Android),
        BackendDeveloper("Backend Developer", Icons.Outlined.Language),
        Designer("Designer", Icons.Outlined.DesignServices),
    }

    enum class Developers(
        val devName: String,
        val type: DeveloperType,
        val email: String,
        val twitter: String? = null,
        val github: String? = null,
        val linkedin: String? = null,
        val telegram: String? = null,
        val website: String? = null
    ) {
        YounesBouhouche(
            "Younes Bouhouche",
            DeveloperType.AndroidDeveloper,
            "y.bouhouche@esi-sba.dz",
            "younesbouh_05",
            "younesbouhouche",
            "younesbouhouche",
            "https://younesbouhouche.github.io"
        ),
        AbderrahmaneAbdat(
            "Abderrahmane Abdat",
            DeveloperType.AndroidDeveloper,
            "a.abdat@esi-sba.dz",
            null,
            null,
            null,
            null
        ),
        OussamaChatri(
            "Oussama Chatri",
            DeveloperType.AndroidDeveloper,
            "o.chatri@esi-sba.dz",
            null,
            null,
            null,
            null
        ),
        AmineLachi(
            "Amine Lachi",
            DeveloperType.BackendDeveloper,
            "ma.lachi@esi-sba.dz",
            null,
            null,
            null,
            null
        ),
        MounirKhabchache(
            "Mounir Khabchache",
            DeveloperType.BackendDeveloper,
            "m.khabchache@esi-sba.dz",
            null,
            null,
            null,
            null
        ),
        NadjetBennaceur(
            "Nadjet Bennaceur",
            DeveloperType.Designer,
            "n.bennaceur@esi-sba.dz",
            null,
            null,
            null,
            null
        )
    }
}