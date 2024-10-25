@file:OptIn(ExperimentalSharedTransitionApi::class)

package com.briak.mealsmenu.presentation.screens.overview

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.briak.mealsmenu.R
import com.briak.mealsmenu.domain.categories.CategoryModel
import com.briak.mealsmenu.domain.meals.MealModel
import com.briak.mealsmenu.presentation.common.message.ErrorUiModel
import com.briak.mealsmenu.presentation.components.ErrorEvents
import com.briak.mealsmenu.presentation.components.ErrorFull
import com.briak.mealsmenu.presentation.navigation.NavLink
import com.briak.mealsmenu.presentation.navigation.NavigationEvents
import com.briak.mealsmenu.presentation.screens.overview.categories.CategoryCard
import com.briak.mealsmenu.presentation.screens.overview.categories.CategoryCardEvents
import com.briak.mealsmenu.presentation.screens.overview.categories.CategoryCardPreviewData
import com.briak.mealsmenu.presentation.screens.overview.categories.CategoryUiModel
import com.briak.mealsmenu.presentation.screens.overview.meals.MealCard
import com.briak.mealsmenu.presentation.screens.overview.meals.MealCardEvents
import com.briak.mealsmenu.presentation.screens.overview.meals.MealCardPreviewData
import com.briak.mealsmenu.presentation.screens.overview.meals.MealUiModel
import com.briak.mealsmenu.presentation.theme.AppTheme
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalSharedTransitionApi::class)
@Composable
fun OverviewScreen(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    events: NavigationEvents,
) {
    val viewModel = koinViewModel<OverviewViewModel>()
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
            object : OverviewScreenEvent {
                override fun onCategoryCardClicked(uiModel: CategoryUiModel) = viewModel.selectCategory(uiModel)

                override fun onMealCardClicked(uiModel: MealUiModel) {
                    viewModel.selectMeal(uiModel)
                    events.navigate(NavLink.mealDetails())
                }

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
                    Text(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 24.dp),
                        text = stringResource(id = R.string.OVERVIEW_SCREEN_TITLE),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
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
                    contentUiModel.loading ->
                        Box(modifier = Modifier.fillMaxSize()) {
                            CircularProgressIndicator(
                                modifier =
                                    Modifier
                                        .align(Alignment.Center)
                                        .size(24.dp),
                                color = MaterialTheme.colorScheme.secondary,
                            )
                        }

                    contentUiModel.error != null ->
                        ErrorFull(
                            modifier = Modifier,
                            errorUiModel = contentUiModel.error,
                            events = eventHandler,
                        )

                    else ->
                        OverviewScreenContent(
                            sharedTransitionScope = sharedTransitionScope,
                            animatedVisibilityScope = animatedVisibilityScope,
                            uiModel = contentUiModel,
                            events = eventHandler,
                        )
                }
            }
        }
    }
}

@Stable
data class OverviewContentUiModel(
    val categories: List<CategoryUiModel>,
    val selectedCategoryId: String?,
    val meals: List<MealUiModel>,
    val loading: Boolean,
    val error: ErrorUiModel?,
) {
    companion object {
        fun create(
            categoryModels: List<CategoryModel>,
            selectedCategoryId: String?,
            mealModels: List<MealModel>,
            loading: Boolean,
            error: Throwable?,
        ) = OverviewContentUiModel(
            categories =
                categoryModels.mapIndexed { index, category ->
                    CategoryUiModel.create(
                        category,
                        selectedCategoryId == category.id,
                        position = index,
                    )
                },
            selectedCategoryId = selectedCategoryId,
            meals = mealModels.mapNotNull { model -> MealUiModel.create(model) },
            loading = loading,
            error = if (error != null) ErrorUiModel(error) else null,
        )
    }
}

interface OverviewScreenEvent :
    CategoryCardEvents,
    MealCardEvents,
    ErrorEvents

@Composable
private fun OverviewScreenContent(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    uiModel: OverviewContentUiModel,
    events: OverviewScreenEvent,
) {
    Column(
        modifier = Modifier.safeContentPadding(),
    ) {
        CategoriesList(uiModel = uiModel, events = events)
        MealsList(
            sharedTransitionScope = sharedTransitionScope,
            animatedVisibilityScope = animatedVisibilityScope,
            uiModel = uiModel,
            events = events,
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategoriesList(
    uiModel: OverviewContentUiModel,
    events: OverviewScreenEvent,
) {
    FlowRow(
        Modifier
            .fillMaxWidth()
            .padding(top = 24.dp),
        horizontalArrangement = Arrangement.spacedBy(AppTheme.dimens.horizontalOffsetXSmall),
    ) {
        repeat(uiModel.categories.size) { index ->
            CategoryCard(
                uiModel = uiModel.categories[index],
                events = events,
            )
        }
    }
}

@Composable
fun MealsList(
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    uiModel: OverviewContentUiModel,
    events: OverviewScreenEvent,
) {
    if (uiModel.loading) { // TODO skeleton loading
        Box(modifier = Modifier.fillMaxSize()) {
            CircularProgressIndicator(
                modifier =
                    Modifier
                        .align(Alignment.Center)
                        .size(24.dp),
                color = MaterialTheme.colorScheme.secondary,
            )
        }
    } else {
        LazyColumn(
            modifier = Modifier.padding(top = 24.dp),
            state = rememberLazyListState(),
        ) {
            items(uiModel.meals) { meal ->
                MealCard(
                    modifier = Modifier.fillMaxWidth(),
                    sharedTransitionScope = sharedTransitionScope,
                    animatedVisibilityScope = animatedVisibilityScope,
                    uiModel = meal,
                    events = events,
                )
                Box(modifier = Modifier.height(8.dp))
            }
        }
    }
}

@Preview
@Composable
private fun OverviewScreenPreview() {
    val visible by remember {
        mutableStateOf(true)
    }

    AppTheme {
        SharedTransitionLayout {
            AnimatedVisibility(visible = visible) {
                OverviewScreenContent(
                    sharedTransitionScope = this@SharedTransitionLayout,
                    animatedVisibilityScope = this@AnimatedVisibility,
                    uiModel =
                        OverviewContentUiModel(
                            categories =
                                listOf(
                                    CategoryCardPreviewData.uiModel1,
                                    CategoryCardPreviewData.uiModel2,
                                ),
                            selectedCategoryId = "1",
                            meals = listOf(MealCardPreviewData.uiModel1, MealCardPreviewData.uiModel2),
                            loading = false,
                            error = null,
                        ),
                    events =
                        object : OverviewScreenEvent {
                            override fun onCategoryCardClicked(uiModel: CategoryUiModel) = Unit

                            override fun onMealCardClicked(uiModel: MealUiModel) = Unit

                            override fun onErrorReloadClicked() = Unit
                        },
                )
            }
        }
    }
}
