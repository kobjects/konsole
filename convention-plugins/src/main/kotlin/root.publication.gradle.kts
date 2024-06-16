import java.util.Properties

plugins {
    id("io.github.gradle-nexus.publish-plugin")
}

allprojects {
    group = "org.kobjects.konsole"
    version = "0.8.0"
}

val secretPropsFile = project.rootProject.file("local.properties")
secretPropsFile.reader().use {
    Properties().apply {
        load(it)
    }
}.onEach { (name, value) ->
    ext[name.toString()] = value
}

fun getExtraString(name: String) = ext[name]?.toString()

nexusPublishing {
    // Configure maven central repository
    // https://github.com/gradle-nexus/publish-plugin#publishing-to-maven-central-via-sonatype-ossrh
    repositories {
        sonatype {
            nexusUrl.set(uri("https://s01.oss.sonatype.org/service/local/"))
            snapshotRepositoryUrl.set(uri("https://s01.oss.sonatype.org/content/repositories/snapshots/"))
            username.set(getExtraString("sonatype.username"))
            password.set(getExtraString("sonatype.password"))
        }
    }
}
