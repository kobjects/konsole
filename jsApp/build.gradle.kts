
plugins {
    kotlin("js")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    implementation(project(":shared"))
}

kotlin {

    js(IR) {
       browser()
        binaries.executable()
    }
}
