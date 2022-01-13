package org.kobjects.konsole.compose

import androidx.compose.runtime.mutableStateListOf
import org.kobjects.konsole.Konsole

class ComposeKonsole : Konsole {
    val entries = mutableStateListOf<Entry>()
    val requests = mutableStateListOf<Request>()

    override fun print(s: String) {
        entries.add(Entry(s, input = false))
    }

    override fun read(consumer: (String) -> Unit) {
        requests.add(Request(consumer))
    }


    data class Entry(val value: String, val input: Boolean)

    data class Request(val consumer: (String) -> Unit)

}