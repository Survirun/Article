import com.devlog.convention.configureHiltAndroid
import com.devlog.convention.configureJUnitAndroid
import com.devlog.convention.configureJunit4Android
import com.devlog.convention.configureKotestAndroid
import com.devlog.convention.configureKotlinAndroid

plugins {
    id("com.android.application")
}


configureKotlinAndroid()
configureHiltAndroid()
configureKotestAndroid()
configureJunit4Android()