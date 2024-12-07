plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.firebasetst"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.firebasetst"
        minSdk = 24
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))
    implementation("com.google.firebase:firebase-firestore")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation ("com.google.android.material:material:1.12.0")
    implementation ("androidx.fragment:fragment:1.8.5")
    implementation ("androidx.recyclerview:recyclerview:1.3.1")

    implementation ("com.squareup.okhttp3:okhttp:4.9.0")
    implementation ("org.osmdroid:osmdroid-android:6.1.11")


    // Android Location Services for live location updates
    implementation ("com.google.android.gms:play-services-location:21.3.0") // Check for the latest version


    // AndroidX permissions for handling permissions at runtime
    implementation ("androidx.core:core-ktx:1.10.0")
    implementation (libs.recyclerview)
    implementation(libs.firebase.storage)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}