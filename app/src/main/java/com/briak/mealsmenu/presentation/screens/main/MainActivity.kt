package com.briak.mealsmenu.presentation.screens.main

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.briak.mealsmenu.presentation.navigation.BottomScreens
import com.briak.mealsmenu.presentation.navigation.FavouritesNavigationEvent
import com.briak.mealsmenu.presentation.navigation.FavouritesScreens
import com.briak.mealsmenu.presentation.navigation.MealDetails
import com.briak.mealsmenu.presentation.navigation.NavigationAnim
import com.briak.mealsmenu.presentation.navigation.OverviewNavigationEvent
import com.briak.mealsmenu.presentation.navigation.OverviewScreens
import com.briak.mealsmenu.presentation.screens.favourites.FavouritesScreen
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

        val bottomScreens =
            remember {
                listOf(
                    BottomScreens.Overview,
                    BottomScreens.Favourites,
                )
            }

        Scaffold(
            bottomBar = {
                NavigationBar {
                    val navBackStackEntry by baseNavController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    bottomScreens.forEach { item ->
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = ImageVector.vectorResource(item.icon),
                                    contentDescription = item.name,
                                )
                            },
                            label = { Text(item.name) },
                            selected =
                                currentDestination?.hierarchy?.any { it.route == item.route::class.qualifiedName }
                                    ?: false,
                            onClick = {
                                baseNavController.navigate(item.route) {
                                    popUpTo(baseNavController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                        )
                    }
                }
            },
        ) { innerPadding ->
            SharedTransitionLayout {
                NavHost(
                    navController = baseNavController,
                    startDestination = OverviewScreens,
                    enterTransition = { NavigationAnim.forthEnter(targetState.destination.route) },
                    exitTransition = { NavigationAnim.forthExit(targetState.destination.route) },
                    popEnterTransition = { NavigationAnim.backEnter(initialState.destination.route) },
                    popExitTransition = { NavigationAnim.backExit(initialState.destination.route) },
                ) {
                    navigation<OverviewScreens>(startDestination = BottomScreens.Overview) {
                        composable<BottomScreens.Overview> {
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
                    navigation<FavouritesScreens>(startDestination = BottomScreens.Favourites) {
                        composable<BottomScreens.Favourites> {
                            FavouritesScreen(
                                sharedTransitionScope = this@SharedTransitionLayout,
                                animatedVisibilityScope = this@composable,
                            ) {
                                handleFavouritesNavigation(it, baseNavController)
                            }
                        }
                    }
                }
            }
        }
    }

    private fun handleOverviewNavigation(
        event: OverviewNavigationEvent,
        navController: NavController,
    ) {
        when (event) {
            is OverviewNavigationEvent.OnMealClicked -> navController.navigate(MealDetails)
            else -> navController.popBackStack()
        }
    }

    private fun handleFavouritesNavigation(
        event: FavouritesNavigationEvent,
        navController: NavController,
    ) {
        when (event) {
            is FavouritesNavigationEvent.OnMealClicked -> navController.navigate(MealDetails)
            else -> navController.popBackStack()
        }
    }
}
