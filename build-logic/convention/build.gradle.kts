plugins {
    `kotlin-dsl`
    `kotlin-dsl-precompiled-script-plugins`
}
dependencies {

    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)

}


gradlePlugin {
    plugins {
        register("androidHilt") {
            id = "jun.android.hilt"
            implementationClass = "com.devlog.AndroidMultiModuleExample.HiltAndroidPlugin"
        }
        register("kotlinHilt") {
            id = "devlog.kotlin.hilt"
            implementationClass = "com.devlog.AndroidMultiModuleExample.HiltKotlinPlugin"
        }
        register("androidRoom") {
            id = "jun.android.room"
            implementationClass = "com.devlog.AndroidMultiModuleExample.AndroidRoomPlugin"
        }

    }
}
