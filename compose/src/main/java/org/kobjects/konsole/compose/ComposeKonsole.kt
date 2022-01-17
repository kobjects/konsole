package org.kobjects.konsole.compose

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import org.kobjects.konsole.Konsole
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


class ComposeKonsole : Konsole, ViewModel() {
    val entries = mutableStateListOf<Entry>()
    val requests = mutableStateListOf<Request>()

    override fun write(s: String) {
        entries.add(Entry(s, input = false))
    }

    override fun readThen(
        label: String,
        validation: (String) -> String,
        consumer: (String) -> Unit) {
        requests.add(Request(label, validation, consumer))
    }

    data class Entry(val value: String, val input: Boolean)

    data class Request(
        val label: String,
        val validation: (String) -> String,
        val consumer: (String) -> Unit)
}
