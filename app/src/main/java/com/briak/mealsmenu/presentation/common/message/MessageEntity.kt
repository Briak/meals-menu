package com.briak.mealsmenu.presentation.common.message

import android.content.Context

interface MessageEntity {
    fun messageText(context: Context): String?
}
