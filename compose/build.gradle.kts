plugins {
    id("com.android.library")
    kotlin("android")
    id("maven-publish")
}


group = "org.kobjects.konsole"
version = "0.2.4"


dependencies {
    implementation("androidx.compose.compiler:compiler:1.3.2")
}

android {

    namespace = "org.kobjects.konsole.compose"
    compileSdk = 33

    defaultConfig {
        minSdk = 21
        targetSdk = 33
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.3.2"
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    publishing {
        multipleVariants {
            allVariants()
        }
    }
}


publishing {
    publications {
        register<MavenPublication>("release") {
            groupId = "org.kobjects.konsole"
            artifactId = "compose"
            version = "0.2.4"

            afterEvaluate {
                from(components["release"])
            }
        }
        repositories {
            mavenLocal()
        }
    }

}

dependencies {
    implementation(project(":core"))
    implementation("androidx.activity:activity-compose:1.6.1")
    implementation("androidx.compose.ui:ui:1.3.2")
    // Tooling support (Previews, etc.)
    implementation("androidx.compose.ui:ui-tooling:1.3.2")
    // Foundation (Border, Background, Box, Image, Scroll, shapes, animations, etc.)
    implementation("androidx.compose.foundation:foundation:1.3.1")
    // Material Design
    implementation("androidx.compose.material:material:1.3.1")
    // Material design icons
    implementation("androidx.compose.material:material-icons-core:1.3.1")
    implementation("androidx.compose.material:material-icons-extended:1.3.1")
    implementation("androidx.appcompat:appcompat:1.5.1")
}


