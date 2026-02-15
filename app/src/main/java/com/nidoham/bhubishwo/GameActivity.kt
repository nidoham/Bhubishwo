package com.nidoham.bhubishwo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.nidoham.bhubishwo.presentation.screen.GameScreen
import com.nidoham.bhubishwo.ui.theme.BhubishwoTheme

class GameActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BhubishwoTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color(0xFF1A1F2E)
                ) {
                    GameScreen()
                }
            }
        }
    }
}