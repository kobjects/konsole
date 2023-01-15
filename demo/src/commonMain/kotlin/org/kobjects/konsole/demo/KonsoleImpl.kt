package org.kobjects.konsole

import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

/** Helper for iOS */
class KonsoleImpl : Konsole {
    var writeFunction: (String) -> Unit = {}
    var readFunction: (String?, (String) -> Unit) -> Unit = { label, fn -> }

    override fun write(s: String) {
        writeFunction(s)
    }

    override suspend fun read(label: String?) = suspendCoroutine<String> { cont ->
        readFunction(label) { cont.resume(it) }
    }

}