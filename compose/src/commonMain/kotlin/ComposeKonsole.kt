package org.kobjects.konsole.compose


import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.suspendCancellableCoroutine
import org.kobjects.konsole.Konsole
import kotlin.coroutines.resume


class ComposeKonsole : Konsole() {
    val entries = mutableStateListOf<Entry>()
    var requests = mutableStateListOf<Request>()

    override fun println(message: Any?) {
        entries.add(Entry(message?.toString() ?: "null", input = false))
    }

    override fun readValidated(message: String?, validation: (String) -> String, callback: (String) -> Unit) {
        requests.add(Request(message, validation) { callback(it) })
    }

    override suspend fun readValidated(message: String?, validation: (String) -> String) = suspendCancellableCoroutine<String> { continuation ->
        val request = Request(message, validation) { continuation.resume(it) }
       requests.add(request)
        continuation.invokeOnCancellation {
            requests.remove(request)
        }
    }


    data class Entry(val value: String, val input: Boolean)

    data class Request(
        val label: String?,
        val validation: (String) -> String,
        val consumer: (String) -> Unit)
}
