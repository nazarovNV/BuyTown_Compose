package com.example.buytowncompose.presentation.auth

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.imageResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.example.buytowncompose.R
import kotlin.math.roundToInt
import kotlin.math.sin

private const val COLUMNS = 4
private const val ROWS = 2
private const val FRAME_COUNT = COLUMNS * ROWS

/** Decorative UI motion stays in Compose; it never changes authentication state. */
@Composable
fun ShoppingOtterLogo(modifier: Modifier = Modifier) {
    val sprite = ImageBitmap.imageResource(R.drawable.otter_shopping_run)
    val description = stringResource(R.string.shopping_otter_description)
    val trackColor = MaterialTheme.colorScheme.primary
    val shadowColor = MaterialTheme.colorScheme.onSurface
    val lifecycleOwner = LocalLifecycleOwner.current
    val lifecycleState by lifecycleOwner.lifecycle.currentStateFlow.collectAsState()
    // Removing the animation when the screen is stopped also stops its frame work.
    val phase = if (lifecycleState.isAtLeast(Lifecycle.State.STARTED)) {
        val transition = rememberInfiniteTransition(label = "Shopping otter run")
        val progress = transition.animateFloat(
            initialValue = 0f,
            targetValue = FRAME_COUNT.toFloat(),
            animationSpec = infiniteRepeatable(tween(720, easing = LinearEasing), RepeatMode.Restart),
            label = "Run cycle"
        )
        progress
    } else {
        remember { androidx.compose.runtime.mutableFloatStateOf(0f) }
    }

    Canvas(modifier.size(width = 208.dp, height = 156.dp)
        .testTag("auth.otter")
        .semantics { contentDescription = description }) {
        // Read progress during drawing, avoiding recomposition of the form on every frame.
        val progress = phase.value
        val frame = progress.toInt().coerceIn(0, FRAME_COUNT - 1)
        val cellWidth = sprite.width / COLUMNS
        val cellHeight = sprite.height / ROWS
        val beat = sin(progress * Math.PI.toFloat() / 2f)
        val groundY = size.height * 0.92f
        drawOval(
            color = shadowColor.copy(alpha = 0.09f),
            topLeft = Offset(size.width * 0.32f, groundY - 4.dp.toPx()),
            size = Size(size.width * (0.36f + beat * 0.015f), 7.dp.toPx())
        )
        // A short moving track gives the stationary logo a forward-running direction.
        val spacing = size.width / 5f
        repeat(6) { index ->
            val x = index * spacing - (progress / FRAME_COUNT) * spacing
            drawLine(trackColor.copy(alpha = 0.16f), Offset(x, groundY + 5.dp.toPx()),
                Offset(x + 12.dp.toPx(), groundY + 5.dp.toPx()), strokeWidth = 2.dp.toPx())
        }
        val drawHeight = size.height * 0.92f
        val drawWidth = drawHeight * cellWidth / (cellHeight * 0.80f)
        // Uniform crop removes only transparent top/bottom padding, retaining a stable frame anchor.
        drawImage(
            image = sprite,
            srcOffset = IntOffset((frame % COLUMNS) * cellWidth,
                (frame / COLUMNS) * cellHeight + (cellHeight * 0.125f).roundToInt()),
            srcSize = IntSize(cellWidth, (cellHeight * 0.80f).roundToInt()),
            dstOffset = IntOffset(((size.width - drawWidth) / 2f).roundToInt(), 0),
            dstSize = IntSize(drawWidth.roundToInt(), drawHeight.roundToInt()),
            filterQuality = FilterQuality.High
        )
    }
}
