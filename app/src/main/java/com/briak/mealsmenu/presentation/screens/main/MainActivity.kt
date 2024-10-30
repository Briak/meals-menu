package com.briak.mealsmenu.presentation.screens.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.briak.mealsmenu.presentation.navigation.MealDetails
import com.briak.mealsmenu.presentation.navigation.NavigationAnim
import com.briak.mealsmenu.presentation.navigation.Overview
import com.briak.mealsmenu.presentation.navigation.OverviewNavigationEvent
import com.briak.mealsmenu.presentation.screens.mealdetails.MealDetailsScreen
import com.briak.mealsmenu.presentation.screens.overview.OverviewScreen
import com.briak.mealsmenu.presentation.theme.AppTheme

@OptIn(ExperimentalSharedTransitionApi::class)
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    Navigation()
                }
            }
        }
    }

    @Composable
    private fun Navigation() {
        val baseNavController = rememberNavController()

        SharedTransitionLayout {
            NavHost(
                navController = baseNavController,
                startDestination = Overview,
                enterTransition = { NavigationAnim.forthEnter(targetState.destination.route) },
                exitTransition = { NavigationAnim.forthExit(targetState.destination.route) },
                popEnterTransition = { NavigationAnim.backEnter(initialState.destination.route) },
                popExitTransition = { NavigationAnim.backExit(initialState.destination.route) },
            ) {
                composable<Overview> {
                    OverviewScreen(
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@composable,
                    ) {
                        handleOverviewNavigation(it, baseNavController)
                    }
                }
                composable<MealDetails> {
                    MealDetailsScreen(
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@composable,
                    ) {
                        handleOverviewNavigation(it, baseNavController)
                    }
                }
            }
        }
    }

    fun handleOverviewNavigation(
        event: OverviewNavigationEvent,
        navController: NavController,
    ) {
        when (event) {
            is OverviewNavigationEvent.OnMealClicked -> navController.navigate(MealDetails)
            else -> navController.popBackStack()
        }
    }
}
