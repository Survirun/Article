import com.devlog.convention.configureHiltAndroid
import com.devlog.convention.configureJunit4Android
import com.devlog.convention.configureKotlinAndroid
import com.devlog.convention.configureKotestAndroid


plugins {
    id("com.android.library")
}

configureKotlinAndroid()
configureHiltAndroid()

configureKotestAndroid()
