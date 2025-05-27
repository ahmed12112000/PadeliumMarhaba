plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.hilt.android.plugin)
    id("kotlin-kapt")
    id ("dagger.hilt.android.plugin")

}

android {
    namespace = "com.nevadev.padeliummarhaba.data"
    compileSdk = 34

    defaultConfig {
        minSdk = 26
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation(libs.threetenabp)
    implementation (libs.androidx.compose.compiler)
    implementation (libs.androidx.compose.runtime)
    implementation(libs.squareup.retrofit)
    implementation(libs.squareup.converter.gson)
    implementation(libs.hilt.android)
    implementation(libs.androidx.ui.graphics.android)
    kapt(libs.hilt.android.compiler)
    implementation(project(":domain"))
    implementation (libs.okhttp )
    implementation (libs.okhttp )
    implementation ("com.squareup.retrofit2:retrofit:2.x.x")
    implementation ("com.squareup.retrofit2:converter-gson:2.x.x")
}
