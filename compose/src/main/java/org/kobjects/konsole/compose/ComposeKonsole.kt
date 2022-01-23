package org.kobjects.konsole.compose


import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import org.kobjects.konsole.Konsole
import java.lang.IllegalStateException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class ComposeKonsole : Konsole, ViewModel() {
    val entries = mutableStateListOf<Entry>()
    var request = mutableStateOf<Request?>(null)

    override fun write(s: String) {
        entries.add(Entry(s, input = false))
    }

    override suspend fun read() = suspendCoroutine<String> { cont ->
        if (request.value != null) {
            throw IllegalStateException("Request pending!")
        }
        request.value = Request { cont.resume(it) }
    }


    data class Entry(val value: String, val input: Boolean)

    data class Request(val consumer: (String) -> Unit)
}
