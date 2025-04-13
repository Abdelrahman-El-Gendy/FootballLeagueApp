package com.example.footballleagueapp

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.footballleagueapp.presentation.screens.FootballViewModel
import com.example.footballleagueapp.presentation.screens.detail.CompetitionDetailScreen
import com.example.footballleagueapp.presentation.screens.home.HomeScreen

@Composable
fun FootballApp() {
    val navController = rememberNavController()
    val viewModel: FootballViewModel = viewModel()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                onCompetitionClick = { competition ->
                    navController.navigate("details/${competition.id}")
                }
            )
        }

        composable(
            "details/{competitionId}",
            arguments = listOf(
                navArgument("competitionId")
                { type = NavType.IntType })
        ) { backStackEntry ->
            val competitionId = backStackEntry.arguments?.getInt("competitionId") ?: 0
            val competition = viewModel.getCompetitionById(competitionId)
            CompetitionDetailScreen(
                competition = competition,
                onBack = { navController.popBackStack() })
        }
    }
}