package org.kobjects.konsole.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import org.kobjects.konsole.Konsole
import org.kobjects.konsole.compose.ComposeKonsole
import org.kobjects.konsole.compose.RenderKonsole
import org.kobjects.konsole.demo.konsoleDemos
import rockPaperScissors

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val konsole = ComposeKonsole()

        rockPaperScissors(konsole)

        setContent {
            Render()
        }
    }

    val title = mutableStateOf("")
    val konsole = mutableStateOf<ComposeKonsole>(ComposeKonsole())

    fun show(title: String, demo: (Konsole) -> Unit) {
        this.title.value = title
        konsole.value = ComposeKonsole()
        demo(konsole.value)
    }

    @Composable
    fun Render() {
        if (title.value == "") {
            RenderMenu()
        } else {
            RenderDemo(title.value, konsole.value)
        }
    }

    @Composable
    fun RenderMenu() {
        LazyColumn {
            items(konsoleDemos.entries.toList()) { entry ->
                Button(onClick = { show(entry.key, entry.value) }) {
                    Text(entry.key)
                }
            }
        }
    }

    @Composable
    fun RenderDemo(title: String, konsole: ComposeKonsole) {
        RenderKonsole(konsole = konsole)
    }
    
}

