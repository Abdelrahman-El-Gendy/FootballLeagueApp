package com.example.footballleagueapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.footballleagueapp.ui.theme.FootballLeagueAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FootballLeagueAppTheme {
                FootballApp()
            }
        }
    }

}


