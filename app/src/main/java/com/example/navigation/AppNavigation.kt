package com.example.navigation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ui.components.ManualApplyBottomSheet
import com.example.ui.screens.*
import com.example.viewmodel.AppViewModel

@Composable
fun AppNavigation(
    viewModel: AppViewModel
) {
    val navController = rememberNavController()
    val activeManualApply by viewModel.activeManualApply.collectAsState()

    Box(modifier = Modifier.fillMaxSize()) {
        NavHost(
            navController = navController,
            startDestination = NavRoutes.HOME
        ) {
            composable(NavRoutes.HOME) {
                HomeScreen(
                    viewModel = viewModel,
                    onNavigate = { route -> navController.navigate(route) }
                )
            }

            composable(NavRoutes.SENSITIVITY) {
                SensitivityScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToDevicePicker = { navController.navigate(NavRoutes.DEVICE_LIBRARY) }
                )
            }

            composable(NavRoutes.FIRE_BUTTON) {
                FireButtonScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToDevicePicker = { navController.navigate(NavRoutes.DEVICE_LIBRARY) }
                )
            }

            composable(NavRoutes.DPI) {
                DpiScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToDevicePicker = { navController.navigate(NavRoutes.DEVICE_LIBRARY) }
                )
            }

            composable(NavRoutes.GRAPHICS) {
                GraphicsFpsScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToDevicePicker = { navController.navigate(NavRoutes.DEVICE_LIBRARY) }
                )
            }

            composable(NavRoutes.HUD_STUDIO) {
                CustomHudScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(NavRoutes.OPTIMIZATION) {
                OptimizationScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToDevicePicker = { navController.navigate(NavRoutes.DEVICE_LIBRARY) }
                )
            }

            composable(NavRoutes.TRAINING) {
                HeadshotTrainingScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(NavRoutes.GYROSCOPE) {
                GyroscopeScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToDevicePicker = { navController.navigate(NavRoutes.DEVICE_LIBRARY) }
                )
            }

            composable(NavRoutes.DEVICE_LIBRARY) {
                DeviceLibraryScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() }
                )
            }

            composable(NavRoutes.FAVORITES) {
                FavoritesScreen(
                    viewModel = viewModel,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToRoute = { route -> navController.navigate(route) }
                )
            }
        }

        // Global Step-by-Step Manual Apply Modal / Bottom Sheet
        ManualApplyBottomSheet(
            target = activeManualApply,
            onDismiss = { viewModel.dismissManualApply() }
        )
    }
}
