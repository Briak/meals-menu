package com.briak.mealsmenu.presentation.navigation

interface NavigationEvents {
    fun navigate(link: String)
    fun back()
}