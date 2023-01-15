package org.kobjects.konsole

import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

interface Konsole {

    fun write(s: String)

    suspend fun read(label: String? = null): String
}