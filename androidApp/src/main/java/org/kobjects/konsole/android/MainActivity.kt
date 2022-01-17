package org.kobjects.konsole.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.kobjects.konsole.compose.ComposeKonsole
import org.kobjects.konsole.compose.RenderKonsole
import org.kobjects.konsole.demo.Demo


class MainActivity : AppCompatActivity() {
    val viewModel = DemoViewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Render()
        }
    }

    override fun onBackPressed() {
        if (viewModel.title.value.isEmpty()) {
            super.onBackPressed()
        } else {
            viewModel.title.value = ""
        }
    }

    @Composable
    fun Render() {
        MaterialTheme() {
            if (viewModel.title.value == "") {
                RenderMenu()
            } else {
                RenderDemo(viewModel.title.value, viewModel.konsole.value)
            }
        }
    }

    @Composable
    fun RenderMenu() {
        Column() {
            TopAppBar(
                title = { Text(text = "${Demo.ALL.size} Konsole Apps")})
            LazyColumn(
                Modifier
                    .padding(8.dp)
                    .fillMaxSize(1f)
            ) {
                items(Demo.ALL) { demo ->
                    Button(onClick = { viewModel.show(demo.name, demo.code) }) {
                        Text(demo.name)
                    }
                }
            }
        }
    }

    @Composable
    fun RenderDemo(title: String, konsole: ComposeKonsole) {
        Column() {
            TopAppBar(
                title = { Text(text = title)})
            RenderKonsole(konsole = konsole)
        }
    }
    
}

