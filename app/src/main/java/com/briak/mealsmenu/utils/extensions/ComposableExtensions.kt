package com.briak.mealsmenu.utils.extensions

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.painterResource

@Composable
fun asyncImagePlaceholder(
    @DrawableRes debugPreview: Int,
) = if (LocalInspectionMode.current) {
    painterResource(id = debugPreview)
} else {
    null
}
