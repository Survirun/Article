plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.jetbrains.kotlin.android)
    id ("kotlin-kapt")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.devlog.main"
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
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion ="1.5.0"
    }

}
kapt {
    correctErrorTypes = true
}

dependencies {
    api(projects.core.preference)
    api(projects.core.designsystem)
    api(projects.core.domain)
    api(projects.core.date)
    api(projects.core.util)
    implementation(projects.feature.featureSplash)
    implementation(projects.feature.featureAppWidgetProvider)
    implementation(projects.feature.featureQuestionList)
    implementation(projects.feature.featureArticleList)
    implementation(projects.feature.featureQuestionCompensation)
    implementation(projects.feature.featureQuestionDetail)
    implementation(projects.feature.featureArticleDetailWebview)
    implementation(projects.feature.featureSignIn)
    implementation(projects.feature.featureBookMark)
    implementation(projects.feature.featureMyKeywordsSelect)
    //Coil
    implementation(libs.coil.compose)
    implementation (libs.coil.gif)
    implementation (libs.androidx.activity.compose)
    implementation (platform(libs.androidx.compose.bom))
    implementation (libs.androidx.lifecycle.viewmodel.compose)
    implementation (libs.okhttp)
    implementation (libs.logging.interceptor)
    implementation (libs.androidx.material)
    implementation (libs.androidx.ui)
    implementation (libs.androidx.ui.graphics)

    implementation (libs.androidx.ui.tooling.preview)
    implementation (libs.androidx.material3)
    implementation (libs.lottie.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation ("androidx.compose.runtime:runtime:1.5.2")




    implementation (libs.androidx.activity)
    implementation (libs.androidx.activity.ktx)
    implementation (libs.androidx.fragment)
    implementation (libs.androidx.fragment.ktx)
    implementation (libs.accompanist.systemuicontroller)

    //hilt
    implementation(libs.hilt.android)

    implementation (libs.androidx.hilt.common)
    implementation (libs.androidx.hilt.work)
    implementation (libs.firebase.messaging.ktx)
    implementation (libs.firebase.messaging)
    implementation (platform(libs.firebase.bom))
    implementation (libs.firebase.auth)
    implementation (libs.androidx.hilt.navigation.compose)
    debugImplementation(libs.ui.tooling)
    kapt (libs.hilt.compiler)

    kapt(libs.hilt.android.compiler)




    implementation(libs.androidx.navigation.compose)
    implementation (libs.androidx.hilt.navigation.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)


}