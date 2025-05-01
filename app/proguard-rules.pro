# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile
-keep public class esi.roadside.assistance.client.auth.presentation.NavRoutes
-keep public class esi.roadside.assistance.client.auth.presentation.NavRoutes$*
-keep public class esi.roadside.assistance.client.main.presentation.NavRoutes
-keep public class esi.roadside.assistance.client.main.presentation.NavRoutes$*
-dontwarn org.slf4j.impl.StaticLoggerBinder
-if class androidx.credentials.CredentialManager
-keep class androidx.credentials.playservices.** {
  *;
}
-keepnames class esi.roadside.assistance.client.auth.presentation.NavRoutes
-keepnames class * extends esi.roadside.assistance.client.auth.presentation.NavRoutes
-keep public class esi.roadside.assistance.client.main.domain.PolymorphicNotification
-keep public class esi.roadside.assistance.client.main.domain.PolymorphicNotification.*