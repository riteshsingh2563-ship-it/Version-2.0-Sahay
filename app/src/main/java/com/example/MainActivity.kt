package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import com.example.ui.SahayApp
import com.example.ui.theme.SahayTheme
import com.example.ui.viewmodel.SahayViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: SahayViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SahayTheme {
                SahayApp(viewModel = viewModel)
            }
        }
    }
}
