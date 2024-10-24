package com.briak.mealsmenu.presentation.screens.main

import android.net.Uri
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
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.briak.mealsmenu.presentation.navigation.NavConst
import com.briak.mealsmenu.presentation.navigation.NavigationAnim
import com.briak.mealsmenu.presentation.navigation.NavigationEvents
import com.briak.mealsmenu.presentation.navigation.removeScheme
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

        val eventHandler =
            remember {
                object : NavigationEvents {
                    override fun navigate(link: String) {
                        val uri = Uri.parse(link)
                        when (uri.scheme) {
                            NavConst.APP_SCHEME -> {
                                baseNavController.navigate(uri.removeScheme())
                            }
                        }
                    }

                    override fun back() {
                        baseNavController.popBackStack()
                    }
                }
            }

        SharedTransitionLayout {
            NavHost(
                navController = baseNavController,
                startDestination = NavConst.APP_OVERVIEW,
                route = "base",
                enterTransition = { NavigationAnim.forthEnter(targetState.destination.route) },
                exitTransition = { NavigationAnim.forthExit(targetState.destination.route) },
                popEnterTransition = { NavigationAnim.backEnter(initialState.destination.route) },
                popExitTransition = { NavigationAnim.backExit(initialState.destination.route) },
            ) {
                composable(route = NavConst.APP_OVERVIEW) {
                    OverviewScreen(
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@composable,
                        eventHandler,
                    )
                }
                composable(route = NavConst.APP_MEAL_DETAILS) {
                    MealDetailsScreen(
                        sharedTransitionScope = this@SharedTransitionLayout,
                        animatedVisibilityScope = this@composable,
                        eventHandler,
                    )
                }
            }
        }
    }
}
