package org.kobjects.konsole

import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class KonsoleImpl : Konsole {
    var writeFunction: (String) -> Unit = {}
    var readFunction: ((String) -> Unit) -> Unit = {}

    override fun write(s: String) {
        writeFunction(s)
    }

    override suspend fun read() = suspendCoroutine<String> { cont ->
        readFunction { cont.resume(it) }
    }
}