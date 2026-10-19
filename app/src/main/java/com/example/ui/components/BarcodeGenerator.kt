package com.example.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.random.Random

/**
 * Generates and renders a realistic 1D barcode on Compose Canvas for checkout scanning.
 */
@Composable
fun BarcodeView(
    code: String,
    modifier: Modifier = Modifier
) {
    // Generate deterministic bar widths based on code hash
    val barPattern = remember(code) {
        val seed = code.hashCode().toLong()
        val rand = Random(seed)
        val list = mutableListOf<Float>()
        for (i in 0 until 55) {
            val width = if (i % 2 == 0) (rand.nextInt(1, 4)).toFloat() else (rand.nextInt(1, 3)).toFloat()
            list.add(width)
        }
        list
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(64.dp)
            ) {
                val totalPatternWeight = barPattern.sum()
                val unitWidth = size.width / totalPatternWeight
                var currentX = 0f

                barPattern.forEachIndexed { index, weight ->
                    val barW = weight * unitWidth
                    // Even indices are dark bars, odd are white gaps
                    if (index % 2 == 0) {
                        drawRect(
                            color = Color.Black,
                            topLeft = Offset(currentX, 0f),
                            size = Size(barW, size.height)
                        )
                    }
                    currentX += barW
                }
            }

            Text(
                text = code.chunked(4).joinToString(" "),
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.SemiBold,
                fontSize = 13.sp,
                color = Color.DarkGray,
                modifier = Modifier.padding(top = 6.dp)
            )
        }
    }
}
