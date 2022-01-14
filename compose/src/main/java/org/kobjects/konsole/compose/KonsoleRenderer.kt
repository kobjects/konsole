package org.kobjects.konsole.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

val END_SHAPE = RoundedCornerShape(8.dp).copy(bottomEnd = CornerSize(0))
val START_SHAPE = RoundedCornerShape(8.dp).copy(bottomStart = CornerSize(0))

@Composable
fun RenderKonsole(
    modifier: Modifier = Modifier.fillMaxSize(1f),
    konsole: ComposeKonsole
) {
    var errorMessage = remember { mutableStateOf("") }

    Column(modifier) {
        LazyColumn(Modifier.weight(1f)) {
            itemsIndexed(konsole.entries) { index, entry ->
                Box(modifier = Modifier.fillParentMaxWidth(1f).padding(horizontal = 8.dp, vertical = 4.dp)) {
                    Card (
                        modifier = Modifier.align(if (entry.input) Alignment.CenterEnd else Alignment.CenterStart),
                        shape = if (entry.input) END_SHAPE else START_SHAPE,
                        backgroundColor = if (entry.input) MaterialTheme.colors.primary else MaterialTheme.colors.secondary) {

                        Text(
                            modifier = Modifier.padding(8.dp),
                            text = entry.value,
                            textAlign = if (entry.input) TextAlign.End else TextAlign.Start
                        )
                    }
                }
            }
        }

        val textState = remember { mutableStateOf(TextFieldValue()) }

        val topRequest = if (konsole.requests.isEmpty()) null else konsole.requests.first()

        Row () {
            TextField(
                modifier = Modifier.weight(1f).height(56.dp),
                isError = errorMessage.value != "",
                enabled = topRequest != null,
                value = textState.value,
                label = {
                    if (errorMessage.value != "") {
                        Text(errorMessage.value)
                    } else if (!topRequest?.label.isNullOrEmpty()) {
                        Text(topRequest!!.label)
                    } },
                onValueChange = {
                    textState.value = it
                    if (errorMessage.value != "") {
                        errorMessage.value =
                            topRequest?.validation?.invoke(textState.value.text) ?: ""
                    }
                }
            )
            Button(
                modifier = Modifier.height(56.dp),
                enabled = topRequest != null,
                onClick = {
                    val text = textState.value.text
                    errorMessage.value = topRequest?.validation?.invoke(textState.value.text) ?: ""
                    if (errorMessage.value == "") {
                        konsole.entries.add(ComposeKonsole.Entry(text, true))
                        topRequest!!.consumer(text)
                        textState.value = TextFieldValue()
                        konsole.requests.removeAt(0)
                    }
                }) {
                Text("Enter")
            }
        }
    }
}