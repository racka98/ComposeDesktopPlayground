package skia

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPlacement
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.rememberWindowState
import org.jetbrains.skia.FilterTileMode
import org.jetbrains.skia.ImageFilter
import org.jetbrains.skia.RuntimeEffect
import org.jetbrains.skia.RuntimeShaderBuilder

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
        state = state,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize(1.0f)
                .background(Color(0xFF03080D))
        ) {
            DrawBackground(modifier = Modifier.align(Alignment.Center))
            DrawForeground(modifier = Modifier.align(Alignment.Center))
            Text(text = "Some Text", modifier = Modifier.align(Alignment.Center), color = Color.White)
        }
    }
}

@Composable
private fun DrawBackground(modifier: Modifier = Modifier) {
    val compositeRuntimeEffect = RuntimeEffect.makeForShader(compositeSksl)
    val compositeShaderBuilder = RuntimeShaderBuilder(compositeRuntimeEffect)
    val density = LocalDensity.current.density
    compositeShaderBuilder.uniform(
        "rectangle",
        85f * density, 110f * density, 405.0f * density, 290.0f * density
    )
    compositeShaderBuilder.uniform("radius", 20.0f * density)
    compositeShaderBuilder.child(
        "noise", Shader.makeFractalNoise(
            baseFrequencyX = 0.45f,
            baseFrequencyY = 0.45f,
            numOctaves = 4,
            seed = 2.0f
        )
    )

    Canvas(
        modifier = modifier then Modifier.fillMaxSize()
            .graphicsLayer(
                renderEffect = ImageFilter.makeRuntimeShader(
                    runtimeShaderBuilder = compositeShaderBuilder,
                    shaderNames = arrayOf("content", "blur"),
                    inputs = arrayOf(
                        null, ImageFilter.makeBlur(
                            sigmaX = 20f,
                            sigmaY = 20f,
                            mode = FilterTileMode.DECAL
                        )
                    )
                ).asComposeRenderEffect()
            )
    ) {

        // Circles
        drawCircle(
            brush = Brush.linearGradient(
                colors = listOf(Color(0xFF7A26D9), Color(0xFFE444E1)),
                start = Offset(450.dp.toPx(), 60.dp.toPx()),
                end = Offset(290.dp.toPx(), 190.dp.toPx()),
                tileMode = TileMode.Clamp
            ),
            center = Offset(375.dp.toPx(), 125.dp.toPx()),
            radius = 100.dp.toPx()
        )
        drawCircle(
            color = Color(0xFFEA357C),
            center = Offset(100.dp.toPx(), 265.dp.toPx()),
            radius = 55.dp.toPx()
        )
        drawCircle(
            brush = Brush.linearGradient(
                colors = listOf(Color(0xFFEA334C), Color(0xFFEC6051)),
                start = Offset(180.dp.toPx(), 125.dp.toPx()),
                end = Offset(230.dp.toPx(), 125.dp.toPx()),
                tileMode = TileMode.Clamp
            ),
            center = Offset(205.dp.toPx(), 125.dp.toPx()),
            radius = 25.dp.toPx()
        )
    }
}

@Composable
fun DrawForeground(modifier: Modifier = Modifier) {
    Canvas(modifier = modifier then Modifier.fillMaxSize(1.0f)) {
        drawRoundRect(
            brush = Brush.linearGradient(
                colors = listOf(Color(0x80FFFFFF), Color(0x00FFFFFF), Color(0x00FF48DB), Color(0x80FF48DB)),
                start = Offset(120.dp.toPx(), 110.dp.toPx()),
                end = Offset(405.dp.toPx(), 290.dp.toPx()),
                tileMode = TileMode.Clamp
            ),
            topLeft = Offset(86.dp.toPx(), 111.dp.toPx()),
            size = Size(318.dp.toPx(), 178.dp.toPx()),
            cornerRadius = CornerRadius(20.dp.toPx()),
            style = Stroke(width = 2.dp.toPx()),
        )
    }
}