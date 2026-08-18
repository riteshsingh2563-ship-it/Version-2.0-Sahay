package com.example.ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.components.SahayBottomNav
import com.example.ui.screens.ActivityScreen
import com.example.ui.screens.AuthScreen
import com.example.ui.screens.BookingConfirmationScreen
import com.example.ui.screens.CourierScreen
import com.example.ui.screens.GroceryScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.HomeServicesScreen
import com.example.ui.screens.NotificationsScreen
import com.example.ui.screens.PharmacyScreen
import com.example.ui.screens.ProfileSettingsScreen
import com.example.ui.screens.RideBookingScreen
import com.example.ui.screens.SplashScreen
import com.example.ui.screens.WalletScreen
import com.example.ui.viewmodel.SahayViewModel

@Composable
fun SahayApp(
    viewModel: SahayViewModel
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: "splash"
    val unreadNotifications by viewModel.unreadNotificationsCount.collectAsState()
    val isUserLoggedIn by viewModel.isUserLoggedIn.collectAsState()

    val bottomNavRoutes = listOf("home", "activity", "wallet", "notifications", "profile")
    val showBottomNav = currentRoute in bottomNavRoutes

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            if (showBottomNav) {
                SahayBottomNav(
                    currentRoute = currentRoute,
                    onNavigate = { route ->
                        if (route != currentRoute) {
                            navController.navigate(route) {
                                popUpTo("home") {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    },
                    unreadNotifications = unreadNotifications
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("splash") {
                SplashScreen(
                    onSplashFinished = {
                        if (isUserLoggedIn) {
                            navController.navigate("home") {
                                popUpTo("splash") { inclusive = true }
                            }
                        } else {
                            navController.navigate("auth") {
                                popUpTo("splash") { inclusive = true }
                            }
                        }
                    }
                )
            }

            composable("auth") {
                AuthScreen(
                    onLoginSuccess = { phoneOrEmail ->
                        viewModel.login(phoneOrEmail)
                        navController.navigate("home") {
                            popUpTo("auth") { inclusive = true }
                        }
                    }
                )
            }

            composable("home") {
                HomeScreen(
                    viewModel = viewModel,
                    onNavigate = { targetRoute ->
                        navController.navigate(targetRoute)
                    }
                )
            }

            composable("activity") {
                ActivityScreen(
                    viewModel = viewModel,
                    onNavigateToDetail = { bookingId ->
                        navController.navigate("booking_detail/$bookingId")
                    },
                    onExploreServices = {
                        navController.navigate("home")
                    }
                )
            }

            composable("wallet") {
                WalletScreen(viewModel = viewModel)
            }

            composable("notifications") {
                NotificationsScreen(viewModel = viewModel)
            }

            composable("profile") {
                ProfileSettingsScreen(
                    viewModel = viewModel,
                    onLogout = {
                        viewModel.logout()
                        navController.navigate("auth") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }

            composable("service_rides") {
                RideBookingScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onBookingCreated = { bookingId ->
                        navController.navigate("booking_detail/$bookingId") {
                            popUpTo("home")
                        }
                    }
                )
            }

            composable("service_courier") {
                CourierScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onBookingCreated = { bookingId ->
                        navController.navigate("booking_detail/$bookingId") {
                            popUpTo("home")
                        }
                    }
                )
            }

            composable("service_grocery") {
                GroceryScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onBookingCreated = { bookingId ->
                        navController.navigate("booking_detail/$bookingId") {
                            popUpTo("home")
                        }
                    }
                )
            }

            composable("service_home_services") {
                HomeServicesScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onBookingCreated = { bookingId ->
                        navController.navigate("booking_detail/$bookingId") {
                            popUpTo("home")
                        }
                    }
                )
            }

            composable("service_pharmacy") {
                PharmacyScreen(
                    viewModel = viewModel,
                    onBack = { navController.popBackStack() },
                    onBookingCreated = { bookingId ->
                        navController.navigate("booking_detail/$bookingId") {
                            popUpTo("home")
                        }
                    }
                )
            }

            composable(
                route = "booking_detail/{bookingId}",
                arguments = listOf(navArgument("bookingId") { type = NavType.StringType })
            ) { backStackEntry ->
                val bookingId = backStackEntry.arguments?.getString("bookingId") ?: ""
                BookingConfirmationScreen(
                    bookingId = bookingId,
                    viewModel = viewModel,
                    onBackToHome = {
                        navController.navigate("home") {
                            popUpTo("home") { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
