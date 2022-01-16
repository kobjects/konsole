package org.kobjects.konsole.android

import android.os.Bundle
import android.view.View
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Forum
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.kobjects.konsole.Konsole
import org.kobjects.konsole.compose.ComposeKonsole
import org.kobjects.konsole.compose.RenderKonsole
import demo.Demo
import kotlin.coroutines.CoroutineContext


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
                title = { Text(text = "KonsoleDemo")})
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

