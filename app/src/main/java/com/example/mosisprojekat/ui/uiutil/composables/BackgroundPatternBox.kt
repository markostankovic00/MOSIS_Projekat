package com.example.mosisprojekat.ui.uiutil.composables

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.tooling.preview.Preview
import com.example.mosisprojekat.ui.theme.MosisProjekatTheme

@Composable
fun BackgroundPatternBox(
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit
) {

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    listOf(
                        MaterialTheme.colors.primary,
                        MaterialTheme.colors.background
                    ),
                )
            )
    ) {
        content()
    }
}

@Preview
@Composable
private fun BackgroundPatternPreview() {
    MosisProjekatTheme {
        BackgroundPatternBox {
            Text(
                text = "Background pattern",
                color = MaterialTheme.colors.onBackground,
                style = MaterialTheme.typography.h1,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}