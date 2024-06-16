package org.kobjects.konsole.composedemo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.kobjects.konsole.compose.ComposeKonsole
import org.kobjects.konsole.compose.RenderKonsole
import org.kobjects.konsole.demo.rockPaperScissors


@Composable
@Preview
fun App() {
    MaterialTheme {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xffcccccc))
        ) {
            Scaffold(
                modifier = Modifier
                    .widthIn(0.dp, 640.dp)
                    .align(Alignment.Center),
                topBar = {
                    TopAppBar(title = {
                        Text("Konsole Demo")
                    })
                }) {
                    val konsole = remember { ComposeKonsole() }
                    rockPaperScissors(konsole)
                    RenderKonsole(
                        Modifier.padding(it)
                            .fillMaxSize()
                            .background(Color(0xffeeeeee)),
                        konsole
                )
            }
        }
    }
}
