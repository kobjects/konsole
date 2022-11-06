
plugins {
    kotlin("js")
}

dependencies {
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation(project(":core"))
    implementation(project(":demo"))
}

kotlin {

    js(IR) {
       browser()
        binaries.executable()
    }
}
