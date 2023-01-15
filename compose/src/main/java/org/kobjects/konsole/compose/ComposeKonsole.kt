package org.kobjects.konsole.compose


import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.suspendCancellableCoroutine
import org.kobjects.konsole.Konsole
import java.lang.IllegalStateException
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class ComposeKonsole : Konsole, ViewModel() {
    val entries = mutableStateListOf<Entry>()
    var requests = mutableStateListOf<Request>()
    var inputVisible = mutableStateOf(true)

    override fun write(s: String) {
        entries.add(Entry(s, input = false))
    }

    override suspend fun read(label: String?) = suspendCancellableCoroutine<String> { continuation ->
        val request = Request(label) { continuation.resume(it) }
       requests.add(request)
        continuation.invokeOnCancellation {
            requests.remove(request)
        }
    }


    data class Entry(val value: String, val input: Boolean)

    data class Request(
        val label: String?,
        val consumer: (String) -> Unit)
}
