package UI

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun Interfaz() = application {

    Window(onCloseRequest = ::exitApplication) {
    }
}