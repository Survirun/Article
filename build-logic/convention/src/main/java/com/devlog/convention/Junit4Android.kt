package com.devlog.convention

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureJunit4Android() {
    with(pluginManager) {

    }


    dependencies {
        //"androidTestImplementation"(libs.findLibrary("androidx.ui.test.junit4").get())
        //"androidTestImplementation"(libs.findLibrary("androidx.junit").get())
    }
}

internal class Junit4AndroidPlugin : Plugin<Project> {

    override fun apply(target: Project) {
        with(target) {

        }
    }
}