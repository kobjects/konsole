package org.kobjects.konsole

import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

open class Konsole {

    /** Prints the given message */
    open fun println(message: Any? = ""): Unit = kotlin.io.println(message)

    /**
     * Calls the given callback with input provided by the user.
     *
     * If the supplied validation function returns anything other than an empty string, the input is not
     * accepted and the return value will be shown to the user.
     */
    open fun readValidated(
        message: String? = null,
        validation: (String) -> String,
        callback: (String) -> Unit
    ) {
        var result: String
        while (true) {
            if (message != null) {
                print("$message ")
            }
            result = kotlin.io.readln()
            val validated = validation(result)
            if (validated.isEmpty()) {
                break
            }
            println(validation(result))
        }
        callback(result)
    }

    /**
     * A suspend variant of readValidated. Only override this method to provide additions
     * functionality such as cancellation handling.
     */
    open suspend fun readValidated(message: String? = null, validation: (String) -> String): String =
        suspendCoroutine { continuation ->
            readValidated(message, validation) { continuation.resume(it) }
        }

    /**
     * Calls the given callback with input provided by the user. Depending on the terminal, the message will
     * not be logged.
     */
    fun readln(message: String? = null, callback: (String) -> Unit) = readValidated(message, { "" }, callback)

    suspend fun readln(message: String? = null): String = readValidated(message, { "" })
}