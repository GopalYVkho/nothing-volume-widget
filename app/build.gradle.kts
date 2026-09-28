plugins {
    id("com.android.application")
}

android {
    namespace = "com.gopal.nothingvolume"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.gopal.nothingvolume"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.16.0")
    implementation("androidx.appcompat:appcompat:1.7.1")
}
