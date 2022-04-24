
plugins {
    kotlin("js")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    implementation(project(":core"))
}

kotlin {

    js(IR) {
       browser()
        binaries.executable()
    }
}
