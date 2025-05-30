import org.gradle.api.JavaVersion.VERSION_11

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.com.google.devtools.ksp)
    alias(libs.plugins.kotlin.serialization)
}

android {
    namespace = "esi.roadside.assistance.client"
    compileSdk = 35

    defaultConfig {
        applicationId = "esi.roadside.assistance.client"
        minSdk = 28
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            buildConfigField("String", "BASE_URL", "\"https://roadside-assistance-backend.onrender.com/\"")
            buildConfigField("String", "MAPBOX_GEOCODING", "\"https://api.mapbox.com/search/geocode/v6/\"")
            buildConfigField("String", "MAPBOX_DRIVING", "\"https://api.mapbox.com/directions/v5/mapbox/\"")
            buildConfigField("String", "CLOUDAMPQ_URL", "\"amqps://xcikzbue:mEXrzOw8yh9B4vwXggUbhIhQj2XjPIES@rat.rmq2.cloudamqp.com/xcikzbue\"")
            buildConfigField("String", "WEB_CLIENT_ID", "\"469116526388-l4fltok5uk1qo6gf4jup9eu57m558h6v.apps.googleusercontent.com\"")
            buildConfigField("String", "GEMINI", "\"AIzaSyBE2qwhZ9WhYEA5JLCzKHUOv6sGnDPRq7A\"")
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro",
            )
            buildConfigField("String", "BASE_URL", "\"https://roadside-assistance-backend.onrender.com/\"")
            buildConfigField("String", "MAPBOX_GEOCODING", "\"https://api.mapbox.com/search/geocode/v6/\"")
            buildConfigField("String", "MAPBOX_DRIVING", "\"https://api.mapbox.com/directions/v5/mapbox/\"")
            buildConfigField("String", "CLOUDAMPQ_URL", "\"amqps://xcikzbue:mEXrzOw8yh9B4vwXggUbhIhQj2XjPIES@rat.rmq2.cloudamqp.com/xcikzbue\"")
            buildConfigField("String", "WEB_CLIENT_ID", "\"469116526388-l4fltok5uk1qo6gf4jup9eu57m558h6v.apps.googleusercontent.com\"")
            buildConfigField("String", "GEMINI", "\"AIzaSyBE2qwhZ9WhYEA5JLCzKHUOv6sGnDPRq7A\"")
        }
    }
    compileOptions {
        sourceCompatibility = VERSION_11
        targetCompatibility = VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    implementation(libs.material.kolor)
    implementation(libs.kmpalette.core)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.material.motion.compose.navigation)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.converter.gson)
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.datastore.preferences)
    testImplementation(libs.junit)
    testImplementation(libs.koin.test.junit4)
    implementation(libs.slf4j.simple)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.kotlinx.serialization.json)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.room.ktx)
    ksp(libs.androidx.room.compiler)
    ksp(libs.androidx.room.ktx)
    implementation(libs.androidx.room.runtime)
    implementation(libs.gson)
    implementation(libs.coil.compose)
    implementation(project.dependencies.platform(libs.koin.bom))
    implementation(libs.koin.core)
    implementation(libs.koin.androidx.compose)
    implementation(libs.androidx.material3.adaptive.navigation.suite)
    implementation(libs.androidx.material3.adaptive.navigation.suite.android)
    implementation(libs.mapbox.android)
    implementation(libs.mapbox.maps.compose)
    implementation(libs.play.services.location)
    coreLibraryDesugaring(libs.desugar.jdk.libs)
    implementation(libs.bundles.ktor)
    testImplementation(libs.truth)
    implementation(libs.cloudinary.android)
    implementation(libs.cloudinary.android.download)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.amqp.client)
    implementation(libs.play.services.auth)
    implementation(libs.compose.markdown)
    implementation(libs.androidx.core.splashscreen)

    // ML Kit dependencies for image analysis and vehicle issue detection
    implementation(libs.image.labeling)
    implementation(libs.obj.detection)
    implementation(libs.text.recognition)

    // CameraX dependencies for camera access and processing
    implementation(libs.androidx.camera.core)
    implementation(libs.androidx.camera.camera2)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)

    // Gemini AI for description generation
    implementation(libs.generativeai)

    // Audio recording and processing
    implementation(libs.androidx.media3.common)
    implementation(libs.androidx.media3.exoplayer)


    implementation(libs.vico.compose)
    implementation(libs.vico.compose.m3)
}
