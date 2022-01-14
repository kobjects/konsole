package org.kobjects.konsole.compose

import androidx.compose.runtime.mutableStateListOf
import org.kobjects.konsole.Konsole

class ComposeKonsole : Konsole {
    val entries = mutableStateListOf<Entry>()
    val requests = mutableStateListOf<Request>()

    override fun print(s: String) {
        entries.add(Entry(s, input = false))
    }

    override fun input(label: String, validation: (String) -> String, consumer: (String) -> Unit) {
        requests.add(Request(label, validation, consumer))
    }


    data class Entry(val value: String, val input: Boolean)

    data class Request(
        val label: String,
        val validation: (String) -> String,
        val consumer: (String) -> Unit)

}