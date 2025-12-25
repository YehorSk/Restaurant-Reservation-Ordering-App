import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import java.io.File
import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.hilt.android)
    alias(libs.plugins.devtools.ksp)
    alias(libs.plugins.google.services)
    alias(libs.plugins.firebase.crashlytics)
    alias(libs.plugins.firebase.perf)
}

val localProps = Properties()
val localPropertiesFile = File(rootProject.rootDir,"local.properties")
if (localPropertiesFile.exists() && localPropertiesFile.isFile) {
    localPropertiesFile.inputStream().use {
        localProps.load(it)
    }
}

android {
    namespace = "com.yehorsk.platea"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.yehorsk.platea"
        minSdk = 26
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug{
            isMinifyEnabled = false
            buildConfigField("String", "BASE_URL", localProps.getProperty("LOCAL_SERVER"))
            buildConfigField("String", "BASE_URL_IMG", localProps.getProperty("LOCAL_SERVER_IMG"))
        }
        create("server"){
            isMinifyEnabled = false
            buildConfigField("String", "BASE_URL", localProps.getProperty("ONLINE_SERVER"))
            buildConfigField("String", "BASE_URL_IMG", localProps.getProperty("ONLINE_SERVER_IMG"))
            signingConfig = signingConfigs.getByName("debug")
            isDebuggable = true
        }
        release {
            isMinifyEnabled = true
            isShrinkResources = true
            buildConfigField("String", "BASE_URL", localProps.getProperty("ONLINE_SERVER"))
            buildConfigField("String", "BASE_URL_IMG", localProps.getProperty("ONLINE_SERVER_IMG"))
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlin{
        compilerOptions{
            jvmTarget = JvmTarget.JVM_17
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.androidx.appcompat)

    implementation(libs.libphonenumber)

    // Firebase

    implementation(platform(libs.firebase.bom))

    implementation(libs.firebase.crashlytics)
    implementation(libs.firebase.analytics)
    
    implementation(libs.firebase.messaging)

    implementation(libs.firebase.perf)

    implementation(libs.komposecountrycodepicker)

    implementation(libs.accompanist.permissions)

    implementation(libs.secrets.gradle.plugin)

    debugImplementation(libs.leakcanary)

    // Tab Synchronizer
    implementation(libs.tabsync.compose)

    // The compose calendar library for Android
    implementation(libs.compose)

    implementation(libs.kotlinx.metadata.jvm)

    //Room
    implementation(libs.androidx.room.runtime)
    implementation(libs.androidx.ui.text.google.fonts)
    ksp(libs.androidx.room.compiler)
    implementation(libs.androidx.room.ktx)

    //Navigation
    implementation(libs.androidx.navigation.compose)

    // ViewModel utilities for Compose
    implementation(libs.androidx.lifecycle.viewmodel.compose)

    // Kotlin serialization
    implementation(libs.kotlinx.serialization.json)

    // Retrofit
    implementation(libs.retrofit)

    // Retrofit with Kotlin serialization Converter
    implementation(libs.retrofit2.kotlinx.serialization.converter)
    implementation(libs.okhttp)
    implementation(libs.androidx.datastore.preferences)

    //Dagger-Hilt
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)
    implementation(libs.androidx.hilt.navigation.compose)
    ksp(libs.androidx.hilt.compiler)

    //Timber
    implementation(libs.timber)

    //SplashScreen
    implementation(libs.androidx.core.splashscreen)

    //Coil
    implementation(libs.coil)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)

    //Icons
    implementation(libs.androidx.material.icons.extended)

    implementation(libs.material3.adaptive.navigation.suite)

    implementation(libs.androidx.credentials.core)
    implementation(libs.androidx.credentials.compat)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
}
