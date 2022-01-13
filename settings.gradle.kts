pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        mavenCentral()
    }
}

rootProject.name = "konsole"
include(":androidApp")
include(":shared")
include(":compose")
include(":demo")
