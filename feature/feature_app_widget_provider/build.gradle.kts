plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id ("kotlin-kapt")
}

android {
    namespace = "com.devlog.feature_app_widget_provider"
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
    api(projects.core.model)
    api(projects.core.date)
    //glide
    implementation (libs.glide)
    implementation (libs.sandwich)
    //hilt
    implementation(libs.hilt.android)
    implementation (libs.androidx.hilt.common)
    implementation (libs.androidx.hilt.work)
    implementation (libs.firebase.messaging.ktx)
    implementation (libs.firebase.messaging)
    implementation (libs.androidx.work.runtime.ktx)

    kapt (libs.hilt.compiler)

    kapt(libs.hilt.android.compiler)

    implementation (libs.glide.transformations)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}