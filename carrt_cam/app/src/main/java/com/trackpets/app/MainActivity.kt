package com.trackpets.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.trackpets.app.presentation.TrackPetsApp
import com.trackpets.app.theme.TrackPetsTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TrackPetsTheme {
                TrackPetsApp()
            }
        }
    }
}
