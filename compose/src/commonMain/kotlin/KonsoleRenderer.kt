package org.kobjects.konsole.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
    modifier: Modifier = Modifier.fillMaxSize(),
    konsole: ComposeKonsole
) {
    var errorMessage = remember { mutableStateOf("") }

    Column(modifier) {
        val listState = rememberLazyListState()
        LaunchedEffect(konsole.entries.size) {
            listState.animateScrollToItem(konsole.entries.size)
        }
        LazyColumn(Modifier.weight(1f), state = listState) {
            itemsIndexed(konsole.entries) { index, entry ->
                Box(modifier = Modifier
                    .fillParentMaxWidth(1f)
                    .padding(horizontal = 8.dp, vertical = 4.dp)) {
                    Card (
                        modifier = Modifier.align(if (entry.input) Alignment.CenterEnd else Alignment.CenterStart),
                        shape = if (entry.input) END_SHAPE else START_SHAPE,
                        backgroundColor = if (entry.input) MaterialTheme.colors.primary else MaterialTheme.colors.surface) {

                        Text(
                            AnsiConverter.ansiToAnnotatedString(entry.value),
                            modifier = Modifier.padding(8.dp),
                            textAlign = if (entry.input) TextAlign.End else TextAlign.Start
                        )
                    }
                }
            }
        }

        val textState = remember { mutableStateOf(TextFieldValue()) }


        fun submit() {
            val text = textState.value.text
            konsole.entries.add(ComposeKonsole.Entry(text, true))
            val request = konsole.requests.last()

            textState.value = TextFieldValue()
            konsole.requests.removeLast()
            request.consumer(text)
        }

        if (konsole.inputVisible.value) {
            Divider()
            Row(
                Modifier.background(Color(0x77ffffff)),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                val topRequest = konsole.requests.lastOrNull()
                TextField(
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent
                    ),
                    isError = errorMessage.value != "",
                    enabled = konsole.requests.isNotEmpty(),
                    value = textState.value,
                    singleLine = true,
                    keyboardActions = KeyboardActions(
                        onDone = { submit() },
                        onSend = { submit() }),
                    onValueChange = {
                        textState.value = it
                    },
                    label = { if (topRequest != null && topRequest.label != null) Text(topRequest.label) }
                )
                IconButton(
                    modifier = Modifier.padding(4.dp),
                    enabled = konsole.requests.isNotEmpty(),
                    onClick = {
                        submit()
                    }) {
                    // Text("Enter")
                    Icon(Icons.Default.Done, "Enter")
                }
            }
        }
    }
}