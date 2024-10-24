package com.briak.mealsmenu.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.FiniteAnimationSpec
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically
import androidx.compose.ui.unit.IntOffset


object NavigationAnim {

    private const val animDelay = 130
    private const val animDuration = 200
    private val fadeSpec: FiniteAnimationSpec<Float> = tween(animDuration, animDelay)
    private val slideSpec: FiniteAnimationSpec<IntOffset> = tween(animDuration, animDelay)

    private val fadeEnter = fadeIn(fadeSpec)
    private val fadeExit = fadeOut(fadeSpec)

    private val horRoutes = emptyList<String>()
    private val horForthUpper = slideInHorizontally(slideSpec) { it }
    private val horForthLower = slideOutHorizontally(slideSpec) { -it }
    private val horBackUpper = slideOutHorizontally(slideSpec) { it }
    private val horBackLower = slideInHorizontally(slideSpec) { -it }

    private val vertRoutes = emptyList<String>()
    private val vertForthUpper = slideInVertically(slideSpec) { it }
    private val vertForthLower = fadeOut(fadeSpec, 0.99f)
    private val vertBackUpper = slideOutVertically(slideSpec) { it }
    private val vertBackLower = fadeIn(fadeSpec, 0.99f)

    fun forthEnter(route: String?): EnterTransition {
        return when {
            isHorizontal(route) -> horForthUpper
            isVertical(route) -> vertForthUpper
            else -> fadeEnter
        }
    }

    fun forthExit(route: String?): ExitTransition {
        return when {
            isHorizontal(route) -> horForthLower
            isVertical(route) -> vertForthLower
            else -> fadeExit
        }
    }

    fun backExit(route: String?): ExitTransition {
        return when {
            isHorizontal(route) -> horBackUpper
            isVertical(route) -> vertBackUpper
            else -> fadeExit
        }
    }

    fun backEnter(route: String?): EnterTransition {
        return when {
            isHorizontal(route) -> horBackLower
            isVertical(route) -> vertBackLower
            else -> fadeEnter
        }
    }

    private fun isHorizontal(route: String?): Boolean {
        if (route == null) return false
        return horRoutes.any { route.startsWith(it) }
    }

    private fun isVertical(route: String?): Boolean {
        if (route == null) return false
        return vertRoutes.any { route.startsWith(it) }
    }

}