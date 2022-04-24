pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "konsole"
include(":androidApp")
include(":core")
include(":compose")
include(":jsApp")

