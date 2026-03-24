package com.ishaan.quizhive

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.ishaan.quizhive.ui.navigation.NavGraph
import com.ishaan.quizhive.ui.theme.QuizHiveTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            QuizHiveTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}