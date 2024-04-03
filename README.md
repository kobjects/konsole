# Look, no UI!

Many programs don't require a sophisticated UI.

This project provides a super simple Kotlin console API -- and simple console- and compose-based
implementations for Web, Android, iOS and desktop/JVM.

```
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

## Gradle targets for running the demos

`:composeApp:run` -- run the "Desktop" demo as a compose GUI app

`-q --console=plain :demo:jvmRun` -- run the JVM command line demo

`:composeApp:wasmJsBrowserDevelopmentRun` -- run the compose browser demo



