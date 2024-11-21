plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")

}


android {
    namespace = "com.devlog.network"
    compileSdk = 34

    defaultConfig {
        minSdk = 28

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
}


dependencies {
   // api(projects.core.common)
  //  api(projects.core.date)
    api(projects.core.model)
    implementation(projects.core.preference)
    implementation(libs.okhttp)
    implementation ( libs.logging.interceptor)
    implementation(libs.sandwich)
    kapt (libs.hilt.compiler)
    implementation(libs.retrofit)
    implementation( libs.converter.gson)
    kapt(libs.hilt.android.compiler)
    implementation(libs.hilt.android)
    implementation (libs.androidx.hilt.common)
            implementation (libs.androidx.hilt.work)
}