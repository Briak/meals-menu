package com.briak.mealsmenu.presentation.navigation

import android.net.Uri
import java.net.URLEncoder

object NavConst {
    const val APP_SCHEME = "meals"

    const val APP_OVERVIEW = "overview"
    const val APP_MEAL_DETAILS = "meal-details"
}

object NavLink {
    fun overview() = appLink(screen = NavConst.APP_OVERVIEW)

    fun mealDetails() = appLink(screen = NavConst.APP_MEAL_DETAILS)

    private fun appLink(
        screen: String,
        key: String? = null,
        link: String? = null,
    ): String =
        Uri
            .Builder()
            .scheme(NavConst.APP_SCHEME)
            .authority(screen)
            .let { if (key != null) it.path(key) else it }
            .let { if (link != null) it.path(URLEncoder.encode(link, "UTF-8")) else it }
            .build()
            .toString()
}

fun String.removeScheme(): String = Uri.parse(this).removeScheme()

fun Uri.removeScheme(): String = this.schemeSpecificPart.removePrefix("//").removePrefix("/")
