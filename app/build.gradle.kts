plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android.plugin)
    id("kotlin-kapt")
    id("kotlin-parcelize")


}

android {
    namespace = "com.nevadev.padeliummarhaba"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.spofun.padeliummarhaba"
        versionCode = 50
        versionName = "2.0.8-fer.2"
        minSdk = 28
        targetSdk = 34
        vectorDrawables.useSupportLibrary = true

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    kapt {
        correctErrorTypes = true
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
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.viewpager2)
    implementation(libs.mediation.test.suite)
    implementation(libs.androidx.material3.android)
    implementation(libs.identity.jvm)
    implementation(libs.androidx.preference.ktx)
    implementation(libs.core.ktx)
    implementation(libs.androidx.hilt.common)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.coil.compose)
    implementation(libs.material)
    implementation(libs.ui)
    implementation(libs.material3)
    implementation(libs.ui.tooling.preview)
    implementation(libs.androidx.activity.compose.v172)
    implementation(libs.coil.compose.v210)
    implementation(libs.androidx.foundation)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.threetenabp)
    implementation(libs.androidx.ui.v160)
    implementation(libs.androidx.ui.tooling.preview.v160)
    implementation(libs.androidx.material.v171)
    implementation(libs.androidx.material.icons.extended)
    implementation(libs.androidxComposeAnimation)
    implementation (libs.androidx.compose.material)
    implementation (libs.squareup.retrofit)
    implementation (libs.squareup.converter.gson)
    implementation (libs.okhttp )
    implementation (libs.logging.interceptor)
    implementation (libs.androidx.navigation.compose.v273)
    implementation (libs.kotlinx.coroutines.android)
    implementation (libs.material.v143)
    implementation(libs.androidx.material3.v110alpha06)
    implementation (libs.androidx.material3.v290)

    implementation (libs.androidx.material3.v130)
    implementation (libs.androidx.navigation.compose.v273)

    implementation (libs.androidx.compose.material3.material3) // Material3

    implementation(libs.kotlinx.coroutines.android.v160)
    implementation (libs.kotlin.stdlib)
    implementation (libs.kotlin.stdlib.v190)
    implementation (libs.kotlinx.coroutines.core.v160)
    implementation (libs.androidx.core.ktx.v1120)
    implementation (libs.androidx.appcompat.v161)
    implementation (libs.androidx.ui.v143)
    implementation (libs.ui.tooling)
    implementation (libs.androidx.foundation.v143)
    implementation (libs.androidx.material.v174)
    implementation (libs.androidx.navigation.compose.v270)
    implementation (libs.androidx.compose.compiler)
    implementation (libs.androidx.compose.runtime)
    implementation (libs.logging.interceptor.v491)
    implementation (libs.androidx.navigation.compose.v273)
    implementation (libs.converter.gson.v290)
    implementation(kotlin("reflect"))
    implementation(libs.converter.scalars)
    implementation(libs.hilt.android)
    kapt(libs.hilt.android.compiler)
    implementation (project(":data"))
    implementation (project(":domain"))

    implementation ("androidx.hilt:hilt-navigation-compose:1.1.0-alpha01")
    kapt("com.google.dagger:hilt-compiler:2.47")
    implementation ("androidx.compose.runtime:runtime-livedata:1.4.0")
    implementation ("androidx.compose.foundation:foundation:1.4.3" )
    implementation ("androidx.compose.ui:ui:1.4.3")
    implementation ("androidx.compose.ui:ui:1.4.0")
    implementation ("androidx.compose.foundation:foundation:1.4.0" )
    implementation ("androidx.compose.material3:material3:1.0.0")
    implementation ("com.google.code.gson:gson:2.10.1")
    implementation ("com.google.accompanist:accompanist-pager:0.28.0")
    implementation ("com.squareup.moshi:moshi-kotlin:1.12.0")
    implementation ("com.squareup.retrofit2:converter-moshi:2.9.0")
    implementation ("com.squareup.moshi:moshi:1.12.0")
    implementation(libs.okhttp.v4110)
    implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.22") // Update if needed

    implementation("androidx.datastore:datastore-preferences:1.1.2")
    implementation("androidx.datastore:datastore-preferences-core:1.1.2")

    implementation("androidx.datastore:datastore:1.1.2")
    implementation("androidx.datastore:datastore-core:1.1.2")
    implementation(libs.kotlinx.coroutines.core.v160) // or the latest version
    implementation(libs.kotlinx.coroutines.android.v181) // or the latest version
    implementation("androidx.work:work-runtime-ktx:2.8.1")



}




