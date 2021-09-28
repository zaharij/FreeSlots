plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    compileSdk = 30

    defaultConfig {
        applicationId = "com.calendar.freeslots"
        minSdk = 21
        targetSdk = 30
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

    val daggerVersion = "2.36"
    val dagger = "com.google.dagger:dagger:$daggerVersion"
    val daggerCompiler = "com.google.dagger:dagger-compiler:$daggerVersion"

dependencies {

    implementation("androidx.core:core-ktx:1.6.0")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("com.google.android.material:material:1.4.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.1")
    implementation("com.google.code.gson:gson:2.8.6")
    implementation(dagger)
    implementation("androidx.legacy:legacy-support-v4:1.0.0")
    kapt(daggerCompiler)
    implementation("android.arch.paging:runtime:1.0.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.4.0-alpha03")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.4.0-alpha03")

    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.4.0")
}