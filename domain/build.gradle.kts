plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android.plugin)
    id("kotlin-kapt")
}

android {
    namespace = "com.nevadev.padeliummarhaba.domain"
    compileSdk = 34

    defaultConfig {
        minSdk = 26
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.1"
    }

    packaging {
        resources {
            excludes += listOf(
                "/META-INF/versions/9/OSGI-INF/MANIFEST.MF",
                "/META-INF/AL2.0",
                "/META-INF/LGPL2.1"
            )
        }
    }
}

dependencies {
    implementation (libs.androidx.compose.compiler)
    implementation (libs.androidx.compose.runtime)
    implementation(libs.hilt.android)
    implementation(libs.androidx.navigation.runtime)
    kapt(libs.hilt.android.compiler)
    implementation (libs.squareup.retrofit)
    implementation (libs.squareup.converter.gson)
}