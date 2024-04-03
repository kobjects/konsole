package org.kobjects.konsole.composedemo

import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.*
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.kobjects.konsole.compose.ComposeKonsole
import org.kobjects.konsole.compose.RenderKonsole
import org.kobjects.konsole.demo.rockPaperScissors


@Composable
@Preview
fun App() {
    MaterialTheme {
        val konsole = remember { ComposeKonsole() }
        rockPaperScissors(konsole)
        RenderKonsole(konsole = konsole)
    }
}