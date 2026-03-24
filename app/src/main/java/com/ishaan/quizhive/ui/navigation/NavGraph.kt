package com.ishaan.quizhive.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ishaan.quizhive.ui.screens.home.HomeScreen
import com.ishaan.quizhive.ui.screens.quiz.QuizScreen
import com.ishaan.quizhive.ui.screens.result.ResultScreen

@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {

        // 🏠 HOME
        composable("home") {
            HomeScreen(navController)
        }

        // 🎯 QUIZ (FIXED)
        composable(
            route = "quiz/{category}/{difficulty}",
            arguments = listOf(
                navArgument("category") { type = NavType.StringType },
                navArgument("difficulty") { type = NavType.StringType }
            )
        ) { backStackEntry ->

            val category = backStackEntry.arguments
                ?.getString("category")
                ?.toIntOrNull()
                ?.takeIf { it != -1 }

            val difficulty = backStackEntry.arguments
                ?.getString("difficulty")
                ?.takeIf { it != "any" }

            QuizScreen(
                navController = navController,
                category = category,
                difficulty = difficulty
            )
        }

        // 🏁 RESULT (FIXED + CONSISTENT NAMING)
        composable(
            route = "result/{score}/{total}/{timeTaken}",
            arguments = listOf(
                navArgument("score") { type = NavType.IntType },
                navArgument("total") { type = NavType.IntType },
                navArgument("timeTaken") { type = NavType.LongType }
            )
        ) { backStackEntry ->

            ResultScreen(
                score = backStackEntry.arguments?.getInt("score") ?: 0,
                total = backStackEntry.arguments?.getInt("total") ?: 0,
                timeTaken = backStackEntry.arguments?.getLong("timeTaken") ?: 0L,
                onRestart = {
                    navController.popBackStack("home", false)
                }
            )
        }
    }
}