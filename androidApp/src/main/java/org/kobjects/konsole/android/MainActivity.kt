package org.kobjects.konsole.android

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
        MaterialTheme(
            colors = LIGHT_COLORS
        ) {
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
                Modifier.fillMaxSize(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(all = 16.dp)
            ) {
                items(Demo.ALL) { demo ->
                    Button(
                        modifier = Modifier.fillMaxWidth(0.8f),
                        onClick = { viewModel.show(demo.name, demo.code) }) {
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

    companion object {
        val LIGHT_COLORS = Colors(
            primary = Color(0xff46a0ff),
            primaryVariant = Color(0xff0072cb),
            secondary = Color(0xffffa546),
            secondaryVariant = Color(0xffc77612),
            background = Color(0xffdddddd),
            surface = Color(0xffdddddd),
            error = Color(0xffff0000),
            onPrimary = Color(0xffffffff),
            onSecondary = Color(0xffffffff),
            onBackground = Color(0xff000000),
            onSurface = Color(0xffffffff),
            onError = Color(0xffffffff),
            isLight = true)
    }

}

