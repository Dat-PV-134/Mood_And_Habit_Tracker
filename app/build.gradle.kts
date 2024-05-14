plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("kotlin-kapt")
}

kapt {
    correctErrorTypes = true
}

android {
    namespace = "com.rekoj134.moodandhabittracker"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.rekoj134.moodandhabittracker"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        isCoreLibraryDesugaringEnabled = true
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    dataBinding {
        enable = true
    }
    viewBinding {
        enable = true
    }
}

dependencies {
    implementation("com.airbnb.android:lottie:6.4.0")

    implementation("com.intuit.sdp:sdp-android:1.1.1")
    implementation("com.intuit.ssp:ssp-android:1.1.1")

    implementation("com.airbnb.android:epoxy:5.1.4")
    // Add the annotation processor if you are using Epoxy's annotations (recommended)
    kapt("com.airbnb.android:epoxy-processor:5.1.4")
    implementation("com.airbnb.android:epoxy-databinding:5.1.4")

    implementation("com.mikhaellopez:circularprogressbar:3.1.0")

    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

    // The view calendar library
    implementation("com.kizitonwose.calendar:view:2.5.1")
    coreLibraryDesugaring("com.android.tools:desugar_jdk_libs:2.0.4")

    //coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.1")

    //room
    implementation("androidx.room:room-runtime:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")

    //Gson
    implementation("com.google.code.gson:gson:2.8.8")

    // Glide
    implementation(libs.glide)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}