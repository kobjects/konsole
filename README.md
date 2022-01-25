# konsole

Programming is easy -- UI programming is hard!

This project provides a super simple Kotlin console API -- and a bunch of implementaitons for various platforms -- including Web, Android, iOS and a "plain" command line.

```
interface Konsole {
    fun write(s: String)
    suspend fun read(): String
}
```

Currently the shared folder contains a bunch of demo apps (so far mostly based on Basic programs from the 101 book). 

The demos can be seen in action here: https://kobjects.org/konsoledemo/


