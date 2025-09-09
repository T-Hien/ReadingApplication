plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)

    //FireBase
//    id("com.android.application")
    id("com.google.gms.google-services")

}

android {
    namespace = "vn.example.readingapplication"
    compileSdk = 34

    packagingOptions {
        exclude ("META-INF/INDEX.LIST")
    }

    defaultConfig {
        applicationId = "vn.example.readingapplication"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    packaging {
        dex {
            useLegacyPackaging = false
        }
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
        viewBinding = true
        dataBinding = true
    }


}


dependencies {
    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.7.0"))
    implementation("com.google.firebase:firebase-analytics")


    implementation("androidx.recyclerview:recyclerview:1.2.1")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.games.activity)
    implementation(libs.androidx.databinding.common)
//    implementation(libs.generativeai)
    implementation(libs.firebase.functions.ktx)
    implementation(libs.firebase.functions)
    implementation(libs.androidx.camera.lifecycle)
    implementation(libs.androidx.camera.view)
//    implementation(libs.firebase.storage.ktx)
    implementation(libs.androidx.tools.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    // PDF
    implementation("com.github.mhiew:android-pdf-viewer:3.2.0-beta.3")
    implementation("com.github.bumptech.glide:glide:4.12.0")
//    annotationProcessor("com.github.bumptech.glide:compiler:4.12.0")
//    implementation("com.squareup.okhttp3:okhttp:4.9.3")

    // DataBinding
    implementation("androidx.databinding:databinding-runtime:8.7.2")

    // CardView
//    implementation("com.google.android.material:material:1.4.0")

    // Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.9.3")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")

    // Layout
    implementation("androidx.core:core-ktx:1.6.0")

    // Flexbox
    implementation("com.google.android.flexbox:flexbox:3.0.0")

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.0")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.0")

    // EPUB
    implementation(files("libs/epublib-core-latest.jar"))
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.0")

//    implementation("com.positiondev.epublib:epublib-core:3.1")
//    implementation("eu.freme-project:epublib-core:0.5")
    implementation ("org.json:json:20210307")

    // Statistical
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")


    //Camere Search Firebase ML Kit
// https://mvnrepository.com/artifact/com.google.cloud/google-cloud-vision
    implementation("com.google.cloud:google-cloud-vision:3.53.0") {
        exclude(group = "org.apache.httpcomponents", module = "httpclient")
        exclude(group = "org.apache.httpcomponents", module = "httpcore")
        exclude(group = "org.jetbrains.kotlin", module = "kotlin-stdlib")
    }
    //Google Storage
    // https://mvnrepository.com/artifact/com.google.cloud/google-cloud-storage
    implementation("com.google.cloud:google-cloud-storage:2.44.1")
// https://mvnrepository.com/artifact/com.google.auth/google-auth-library-credentials
    implementation("com.google.auth:google-auth-library-credentials:1.30.1")



// https://mvnrepository.com/artifact/com.google.mlkit/image-labeling
    implementation("com.google.mlkit:image-labeling:17.0.7")


    implementation ("androidx.camera:camera-core:1.4.0")
//
//    //Camera
    implementation ("androidx.camera:camera-camera2:1.4.0") // Camera2 Implementation
}
