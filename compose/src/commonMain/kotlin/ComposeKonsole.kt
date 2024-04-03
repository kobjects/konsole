package org.kobjects.konsole.compose


import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.suspendCancellableCoroutine
import org.kobjects.konsole.Konsole
import kotlin.coroutines.resume


class ComposeKonsole : Konsole() {
    val entries = mutableStateListOf<Entry>()
    var requests = mutableStateListOf<Request>()
    var inputVisible = mutableStateOf(true)

    override fun println(message: Any?) {
        entries.add(Entry(message?.toString() ?: "null", input = false))
    }

    override fun readln(message: String?, callback: (String) -> Unit) {
        requests.add(Request(message) { callback(it) })
    }

    override suspend fun readln(message: String?) = suspendCancellableCoroutine<String> { continuation ->
        val request = Request(message) { continuation.resume(it) }
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
