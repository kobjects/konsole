# Look, no UI!

Programming is easy -- UI programming is hard!

This project provides a super simple Kotlin console API -- and a bunch of implementaitons for various platforms -- including Web, Android, iOS and a "plain" command line.

```
interface Konsole {
    fun write(s: String)
    suspend fun read(): String
}
```

The goal is to make it super easy to write simple Kotlin programs that run on all platforms and look "ok" utilizing a chat-like appearence. 

Of course a console API is not the "right" interface for everything, but it might be a good starting point to test
out an idea or to demonstrate a programming concept withou investing heavily into UI development. And there are 
modern applications for this kind of dialog interface, for instance assistant plugins or chat bots.

Currently the shared folder contains a bunch of demo apps (so far mostly based on Basic programs from the 101 book). 

The demos can be seen in action here: https://kobjects.org/konsole/demo/

The reason that the demos are included in the shared folder (opposed to a separate demo module) is a problem with iOS module organization and hopefully 
this can be fixed soon. 

