# Look, no UI!

[![Maven Central](https://maven-badges.herokuapp.com/maven-central/org.kobjects.konsole/core/badge.svg)](https://maven-badges.herokuapp.com/maven-central/org.kobjects.konsole/core)

Many programs don't require a sophisticated UI.

This project provides a super simple Kotlin console API -- and simple console- and compose-based
implementations for Web, Android, iOS and desktop/JVM.

```kt
open class Konsole {
    open fun println(message: Any? = "")
    open fun readln(label: String? = null, callback: (String) -> Unit)
    suspend fun readln(label: String? = null): String  // Implemented based on the callback variant
}
```

The goal is to make it super easy to write simple Kotlin programs that run on all platforms and look
"ok" utilizing a chat-like appearance. 

Of course a console API is not the "right" interface for everything, but it might be a good starting
point to test out an idea or to demonstrate a programming concept without investing heavily into UI
development. And there are modern applications using this kind of dialog interface, for instance 
assistant plugins or chat bots.

Web demo: [https://kobjects.org/konsole](https://kobjects.org/konsole)

Demo application source code: [demo/src/commonMain/kotlin/org/kobjects/konsole/demo/RockPaperScissors.kt](https://github.com/kobjects/konsole/blob/main/demo/src/commonMain/kotlin/org/kobjects/konsole/demo/RockPaperScissors.kt)

Compose application source code: [commonMain/kotlin/App.kt](https://github.com/kobjects/konsole/blob/main/composeApp/src/commonMain/kotlin/App.kt)

## Gradle Targets for Running the Demos

`:composeApp:run` -- run the "Desktop" demo as a compose GUI app

`-q --console=plain :demo:jvmRun` -- run the JVM command line demo

`:composeApp:wasmJsBrowserDevelopmentRun` -- run the compose browser demo

## FAQ

### How do I change the color / formatting? 

Ansi control sequences for changing the text color are supported.

### I want to depend on Konsole only in my in demo, not in my library!

If console IO is part of your library and you don't want to depend on Konsole,
just use generic print and readln functions that can be easily wired up to
Konsole in your web demo.


## Platform Specific Issues

### Android 

- Add `android:windowSoftInputMode="adjustResize"` to the `Activity`-Element in `AndroidManifest.xml` in order 
  to ensure that the console history remains visible during input.




