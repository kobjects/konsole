plugins {
    id("com.android.application")
    kotlin("android")
}

dependencies {
    implementation("androidx.compose.compiler:compiler:1.3.2")
}


android {
    compileSdk = 33
    defaultConfig {
        applicationId = "org.kobjects.konsole.android"
        minSdk = 21
        targetSdk = 33
        versionCode = 2
        versionName = "0.2.3"
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }


    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    namespace = "org.kobjects.konsole.android"
}

dependencies {
    implementation(project(":compose"))
    implementation(project(":core"))
    implementation(project(":demo"))
    implementation("androidx.activity:activity-compose:1.6.1")
    implementation("androidx.compose.ui:ui:1.3.0")
    // Tooling support (Previews, etc.)
    implementation("androidx.compose.ui:ui-tooling:1.3.0")
    // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
    implementation("androidx.compose.foundation:foundation:1.3.0")
    // Material Design
    implementation("androidx.compose.material:material:1.3.0")
    // Material design icons
    implementation("androidx.compose.material:material-icons-core:1.3.0")
    implementation("androidx.compose.material:material-icons-extended:1.3.0")
    implementation("androidx.appcompat:appcompat:1.5.1")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.5.1")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
}