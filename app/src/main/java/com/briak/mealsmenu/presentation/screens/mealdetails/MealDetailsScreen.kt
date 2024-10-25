@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.briak.mealsmenu.presentation.screens.mealdetails

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.briak.mealsmenu.R
import com.briak.mealsmenu.domain.meals.MealModel
import com.briak.mealsmenu.presentation.common.message.ErrorUiModel
import com.briak.mealsmenu.presentation.components.ErrorEvents
import com.briak.mealsmenu.presentation.components.ErrorFull
import com.briak.mealsmenu.presentation.navigation.NavigationEvents
import com.briak.mealsmenu.presentation.screens.mealdetails.ingredients.IngredientItem
import com.briak.mealsmenu.presentation.screens.overview.meals.MealFullPreviewData
import com.briak.mealsmenu.presentation.screens.overview.meals.MealUiModel
import com.briak.mealsmenu.presentation.theme.AppTheme
import com.briak.mealsmenu.utils.extensions.asyncImagePlaceholder
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun MealDetailsScreen(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    events: NavigationEvents,
) {
    val viewModel = koinViewModel<MealDetailsViewModel>()
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    val contentUiModel = viewModel.screenDataFlow.collectAsState(initial = null).value

    DisposableEffect(lifecycle, viewModel) {
        val observer =
            LifecycleEventObserver { _, event ->
                when (event) {
                    Lifecycle.Event.ON_START -> viewModel.subscribe()
                    Lifecycle.Event.ON_STOP -> viewModel.unsubscribe()
                    else -> Unit
                }
            }
        lifecycle.addObserver(observer)
        onDispose { lifecycle.removeObserver(observer) }
    }

    val eventHandler =
        remember {
            object : MealDetailsScreenEvent {
                override fun onErrorReloadClicked() = viewModel.reload()
            }
        }

    Scaffold(
        topBar = {
            TopAppBar(
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                        titleContentColor = MaterialTheme.colorScheme.primary,
                    ),
                title = {
                    with(sharedTransitionScope) {
                        Text(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 24.dp)
                                    .sharedBounds(
                                        rememberSharedContentState(key = "title-${contentUiModel?.meal?.id ?: ""}"),
                                        animatedVisibilityScope = animatedVisibilityScope,
                                    ),
                            text = contentUiModel?.meal?.name ?: "",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { events.back() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Arrow back",
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Surface(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding),
            color = MaterialTheme.colorScheme.background,
        ) {
            if (contentUiModel != null) {
                when {
                    contentUiModel.loading -> MealDetailsLoader()
                    contentUiModel.error != null -> ErrorFull(Modifier, contentUiModel.error, eventHandler)
                    else ->
                        MealDetailsContent(
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = animatedVisibilityScope,
                            uiModel = contentUiModel.meal!!,
                        )
                }
            }
        }
    }
}

interface MealDetailsScreenEvent : ErrorEvents

@Composable
fun MealDetailsLoader() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier =
                Modifier
                    .align(Alignment.Center)
                    .size(24.dp),
            color = MaterialTheme.colorScheme.secondary,
        )
    }
}

@Composable
fun MealDetailsContent(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    uiModel: MealUiModel,
) {
    LazyColumn(
        modifier = Modifier.safeContentPadding(),
    ) {
        item {
            with(sharedTransitionScope) {
                AsyncImage(
                    placeholder = asyncImagePlaceholder(debugPreview = R.drawable.ic_launcher_background),
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(top = 24.dp)
                            .height(240.dp)
                            .sharedElement(
                                state = rememberSharedContentState(key = "image-${uiModel.id}"),
                                animatedVisibilityScope = animatedVisibilityScope,
                            ).clip(CircleShape.copy(CornerSize(24.dp))),
                    model =
                        ImageRequest
                            .Builder(LocalContext.current)
                            .data(uiModel.iconUrl)
                            .crossfade(true)
                            .placeholderMemoryCacheKey("image-${uiModel.id}")
                            .memoryCacheKey("image-${uiModel.id}")
                            .build(),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                )
            }
        }
        item {
            Text(
                modifier =
                    Modifier
                        .padding(top = 24.dp, bottom = 8.dp),
                text = stringResource(id = R.string.MEAL_DETAILS_SCREEN_INGREDIENTS_LABEL),
                style = MaterialTheme.typography.titleLarge,
            )
        }
        items(uiModel.ingredients) { ingredient -> IngredientItem(uiModel = ingredient) }
        item {
            Text(
                modifier = Modifier.padding(top = 24.dp),
                text = stringResource(id = R.string.MEAL_DETAILS_SCREEN_INSTRUCTIONS_LABEL),
                style = MaterialTheme.typography.titleLarge,
            )
        }
        item {
            Text(
                modifier = Modifier.padding(top = 8.dp),
                text = uiModel.instructions,
                style = MaterialTheme.typography.bodyMedium,
            )
        }
    }
}

@Stable
data class MealDetailsContentUiModel(
    val meal: MealUiModel?,
    val loading: Boolean,
    val error: ErrorUiModel?,
) {
    companion object {
        fun create(
            mealModel: MealModel?,
            loading: Boolean,
            error: Throwable?,
        ) = MealDetailsContentUiModel(
            meal = MealUiModel.create(mealModel),
            loading = loading,
            error = if (error != null) ErrorUiModel(error) else null,
        )
    }
}

@Preview(
    showBackground = true,
)
@Composable
private fun MealDetailsScreenPreview() {
    val visible by remember {
        mutableStateOf(true)
    }

    AppTheme {
        SharedTransitionLayout {
            AnimatedVisibility(visible = visible) {
                MealDetailsContent(
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@AnimatedVisibility,
                    uiModel = MealFullPreviewData.uiModel1,
                )
            }
        }
    }
}
