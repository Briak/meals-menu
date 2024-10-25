package com.briak.mealsmenu.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.briak.mealsmenu.R
import com.briak.mealsmenu.presentation.common.message.ErrorUiModel

@Composable
fun ErrorFull(
    modifier: Modifier,
    errorUiModel: ErrorUiModel,
    events: ErrorEvents?,
) {
    Box(modifier = modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.safeContentPadding().align(Alignment.Center),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                val message = errorUiModel.messageText(LocalContext.current)
                if (message != null) {
                    Text(
                        text = message,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.titleMedium,
                    )
                }
            }
            if (events != null) {
                item { Spacer(modifier = Modifier.height(24.dp)) }
                item {
                    ElevatedButton(onClick = { events.onErrorReloadClicked() }) {
                        Text(text = stringResource(id = R.string.GENERAL_RELOAD))
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun ErrorFullPreview() {
    ErrorFull(
        modifier = Modifier,
        errorUiModel = ErrorUiModel(Throwable("")),
        events =
            object : ErrorEvents {
                override fun onErrorReloadClicked() = Unit
            },
    )
}
