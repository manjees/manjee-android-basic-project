import gradle.kotlin.dsl.accessors._fa393d92bf243f86e14930a7cb20dbb9.implementation
import gradle.kotlin.dsl.accessors._fa393d92bf243f86e14930a7cb20dbb9.kapt
import gradle.kotlin.dsl.accessors._fa393d92bf243f86e14930a7cb20dbb9.testImplementation

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    defaultConfig {
        compileSdk = Config.compileSdk
        minSdk = Config.minSdk
        targetSdk = Config.targetSdk
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        debug {
            isMinifyEnabled = true
            consumerProguardFiles("proguard-rules.pro")
        }
        release {
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

    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures {
        buildConfig = true
    }
}


val libs: VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

dependencies {
    implementation(libs.getLibrary("hilt.android"))
    kapt(libs.getLibrary("hilt.compiler"))
    implementation(libs.getLibrary("coroutines.android"))

    androidTestImplementation(libs.getLibrary("androidx.junit"))
    androidTestImplementation(libs.getLibrary("androidx.test.runner"))

    testImplementation(libs.getLibrary("junit"))
    testImplementation(libs.getLibrary("coroutine.test"))
    testImplementation(libs.getLibrary("mockk.android"))
}