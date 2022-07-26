package skia

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState

@Composable
fun BlurSkiaWindow(
    exit: () -> Unit
) {

    val state = rememberWindowState(
        placement = WindowPlacement.Floating,
        position = WindowPosition.Aligned(Alignment.Center),
        size = DpSize(510.dp, 370.dp)
    )

    Window(
        title = "Skia Shader Blur",
        onCloseRequest = exit,
        state = state
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(1.0f)
                .background(Color(0xFF03080D))
        ) {
        }
    }
}