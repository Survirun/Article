plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}
dependencies {

    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)

}

dependencies {
    compileOnly(libs.android.gradlePlugin)
   
    compileOnly(libs.kotlin.gradlePlugin)

}
gradlePlugin {
    plugins {
        register("androidApplication") {
            id = "devlog.android.application"
            implementationClass = "Devlog_android_applicationPlugin"
        }
        register("androidHilt") {
            id = "devlog.android.hilt"
            implementationClass = "com.devlog.Article.HiltAndroidPlugin"
        }
        register("kotlinHilt") {
            id = "devlog.kotlin.hilt"
            implementationClass = "com.devlog.Article.HiltKotlinPlugin"
        }
        register("androidRoom") {
            id = "devlog.android.room"
            implementationClass = "com.devlog.Article.AndroidRoomPlugin"
        }

    }
}
