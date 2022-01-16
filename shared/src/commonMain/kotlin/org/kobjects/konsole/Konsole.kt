package org.kobjects.konsole

import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

interface Konsole {


    fun write(s: String)

    suspend fun read(
        label: String = "",
        validation: (String) -> String = {""}
    ): String = suspendCoroutine { cont ->
            readThen(label, validation) { cont.resume(it) }
        }


    fun readThen(
        label: String = "",
        validation: (String) -> String = {""},
        consumer: (String) -> Unit
    )


}