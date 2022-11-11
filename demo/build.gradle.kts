plugins {
    kotlin("multiplatform")
    kotlin("native.cocoapods")
    id("com.android.library")
    id("maven-publish")
}

group = "org.kobjects.konsole.demo"
version = "0.2.2"

kotlin {
    android {
        publishLibraryVariants("release", "debug")
    }
    iosX64()
    iosArm64()
    iosSimulatorArm64() // sure all ios dependencies support this target

    jvm("desktop")

    js(IR) {
   //     useCommonJs()
        browser()
    }


    cocoapods {
        summary = "Some description for the Demo Module"
        homepage = "Link to the Demo Module homepage"
        ios.deploymentTarget = "14.1"
        podfile = project.file("../iosApp/Podfile")
        framework {
            baseName = "demo"
        }
    }
    
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.kobjects.ktxml:core:0.2.2")
                implementation(project(":core"))
            }
        }
        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)
        }
        val desktopMain by getting
        val desktopTest by getting

        val jsMain by getting
        val jsTest by getting
    }
}


android {
    compileSdk = 33
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = 21
        targetSdk = 33
    }
    namespace = "org.kobjects.konsole.demo"
}
