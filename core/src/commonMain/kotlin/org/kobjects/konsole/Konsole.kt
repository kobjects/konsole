package org.kobjects.konsole

import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

open class Konsole {

    /** Prints the given message */
    open fun println(message: Any? = ""): Unit = kotlin.io.println(message)

    /** Calls the given callback with input provided by the user. */
    open fun readln(message: String? = null, callback: (String) -> Unit) {
        if (message != null) {
            kotlin.io.print("$message ")
        }
        callback(kotlin.io.readln())
    }

    /**
     * A suspend variant of readln. Only override this method to provide additions
     * functionality such as cancellation handling.
     */
    open suspend fun readln(message: String? = null): String =
        suspendCoroutine { continuation ->
            readln(message) { continuation.resume(it) }
        }

}