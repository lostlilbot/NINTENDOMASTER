package com.ndsemulator.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ndsemulator.app.ui.screens.emulator.EmulatorScreen
import com.ndsemulator.app.ui.screens.library.LibraryScreen
import com.ndsemulator.app.ui.screens.settings.SettingsScreen
import com.ndsemulator.app.ui.screens.download.DownloadScreen

/**
 * Navigation routes for the app.
 */
sealed class Screen(val route: String) {
    data object Library : Screen("library")
    data object Emulator : Screen("emulator/{gameId}") {
        fun createRoute(gameId: Long) = "emulator/$gameId"
    }
    data object Settings : Screen("settings")
    data object Download : Screen("download")
}

/**
 * Main navigation component using Jetpack Compose Navigation.
 */
@Composable
fun NDSNavigation() {
    val navController = rememberNavController()
    
    NavHost(
        navController = navController,
        startDestination = Screen.Library.route
    ) {
        composable(Screen.Library.route) {
            LibraryScreen(
                onGameClick = { gameId ->
                    navController.navigate(Screen.Emulator.createRoute(gameId))
                },
                onSettingsClick = {
                    navController.navigate(Screen.Settings.route)
                },
                onDownloadClick = {
                    navController.navigate(Screen.Download.route)
                }
            )
        }
        
        composable(
            route = Screen.Emulator.route,
            arguments = listOf(
                navArgument("gameId") { type = NavType.LongType }
            )
        ) { backStackEntry ->
            val gameId = backStackEntry.arguments?.getLong("gameId") ?: 0L
            EmulatorScreen(
                gameId = gameId,
                onBackClick = { navController.popBackStack() },
                onSettingsClick = { navController.navigate(Screen.Settings.route) }
            )
        }
        
        composable(Screen.Settings.route) {
            SettingsScreen(
                onBackClick = { navController.popBackStack() }
            )
        }
        
        composable(Screen.Download.route) {
            DownloadScreen(
                onBackClick = { navController.popBackStack() },
                onDownloadComplete = { navController.popBackStack() }
            )
        }
    }
}
