package org.kobjects.konsole.compose

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
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
    val errorMessage = remember { mutableStateOf("") }
    val submitAttempted = remember { mutableStateOf(false) }

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
                            textAlign = if (entry.input && !entry.value.contains("\n")) TextAlign.End else TextAlign.Start
                        )
                    }
                }
            }
        }

        val textState = remember { mutableStateOf(TextFieldValue()) }

        fun submit() {
            val text = textState.value.text
            val request = konsole.requests.last()
            errorMessage.value = request.validation(text)

            if (errorMessage.value.isEmpty()) {
                submitAttempted.value = false
                konsole.entries.add(ComposeKonsole.Entry(text, true))

                textState.value = TextFieldValue()
                konsole.requests.removeLast()
                request.consumer(text)
            } else {
                submitAttempted.value = true
            }
        }

        if (konsole.requests.isNotEmpty()) {
            val request = konsole.requests.last()
            Divider()
            Row(
                Modifier.background(Color(0x77ffffff)),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                TextField(
                    modifier = Modifier.weight(1f),
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent
                    ),
                    isError = errorMessage.value != "",
                    // enabled = konsole.requests.isNotEmpty(),
                    value = textState.value,
                    singleLine = true,
                    keyboardActions = KeyboardActions(
                        onGo = { submit() },
                        onDone = { submit() },
                        onSend = { submit() }),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Go
                    ),
                    onValueChange = {
                        textState.value = it
                        if (submitAttempted.value) {
                            errorMessage.value = request.validation(it.text)
                        }
                    },
                    label = {
                        if (errorMessage.value.isNotEmpty()) {
                            Text(errorMessage.value)
                        } else if (request.label != null) {
                            Text(request.label)
                        }
                    },
                    trailingIcon = {
                        if (errorMessage.value.isNotEmpty()) {
                            Icon(Icons.Default.Error, errorMessage.value)
                        }
                    }
                )
                IconButton(
                    modifier = Modifier.padding(4.dp),
                    enabled = konsole.requests.isNotEmpty(),
                    onClick = {
                        submit()
                    }) {
                    // Text("Enter")
                    Icon(Icons.Default.SubdirectoryArrowLeft, "Enter")
                }
            }
        }
    }
}