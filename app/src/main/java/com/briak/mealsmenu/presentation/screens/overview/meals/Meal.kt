package com.briak.mealsmenu.presentation.screens.overview.meals

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.AnimatedVisibilityScope
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.briak.mealsmenu.R
import com.briak.mealsmenu.domain.meals.MealModel
import com.briak.mealsmenu.presentation.screens.mealdetails.ingredients.IngredientItemPreviewData
import com.briak.mealsmenu.presentation.screens.mealdetails.ingredients.IngredientUiModel
import com.briak.mealsmenu.presentation.theme.AppTheme
import com.briak.mealsmenu.utils.extensions.asyncImagePlaceholder

@Stable
data class MealUiModel(
    val id: String,
    val name: String,
    val iconUrl: String,
    val ingredients: List<IngredientUiModel>,
    val instructions: String,
    val model: MealModel,
) {
    companion object {
        fun create(mealModel: MealModel?): MealUiModel? {
            if (mealModel == null) return null
            return MealUiModel(
                id = mealModel.id,
                name = mealModel.name,
                iconUrl = mealModel.iconUrl,
                ingredients =
                mealModel.ingredients
                    ?.filter { ingredient -> ingredient.name.isNotEmpty() }
                    ?.map { ingredient -> IngredientUiModel.create(ingredient) }
                    .orEmpty(),
                instructions = mealModel.instructions ?: "",
                model = mealModel,
            )
        }
    }
}

interface MealCardEvents {
    fun onMealCardClicked(uiModel: MealUiModel)
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun MealCard(
    modifier: Modifier = Modifier,
    sharedTransitionScope: SharedTransitionScope,
    animatedVisibilityScope: AnimatedVisibilityScope,
    uiModel: MealUiModel,
    events: MealCardEvents,
) {
    Card(
        modifier =
            modifier
                .clickable { events.onMealCardClicked(uiModel) },
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant,
            ),
    ) {
        Row {
            Box(Modifier.size(100.dp)) {
                with(sharedTransitionScope) {
                    AsyncImage(
                        modifier =
                            Modifier
                                .sharedElement(
                                    state = rememberSharedContentState(key = "image-${uiModel.id}"),
                                    animatedVisibilityScope = animatedVisibilityScope,
                                ).clip(
                                    CircleShape.copy(
                                        topStart = CornerSize(12.dp),
                                        bottomStart = CornerSize(12.dp),
                                        topEnd = CornerSize(0.dp),
                                        bottomEnd = CornerSize(0.dp),
                                    ),
                                ),
                        placeholder = asyncImagePlaceholder(debugPreview = R.drawable.ic_launcher_background),
                        model = uiModel.iconUrl,
                        contentScale = ContentScale.Crop,
                        contentDescription = "",
                    )
                }
            }
            with(sharedTransitionScope) {
                Text(
                    modifier =
                        Modifier
                            .align(Alignment.CenterVertically)
                            .padding(horizontal = 16.dp)
                            .sharedBounds(
                                rememberSharedContentState(key = "title-${uiModel.id}"),
                                animatedVisibilityScope = animatedVisibilityScope,
                            ),
                    text = uiModel.name,
                )
            }
        }
    }
}

object MealCardPreviewData {
    val uiModel1 =
        MealUiModel(
            id = "1",
            name = "Meal 1",
            iconUrl = "https://upload.wikimedia.org/wikipedia/commons/0/0f/Android_phones.jpg",
            ingredients = emptyList(),
            instructions = "",
            model = MealModel("1", "Meal 1", ""),
        )
    val uiModel2 =
        MealUiModel(
            id = "2",
            name = "Meal 2",
            iconUrl = "https://upload.wikimedia.org/wikipedia/commons/0/0f/Android_phones.jpg",
            ingredients = emptyList(),
            instructions = "",
            model = MealModel("2", "Meal 2", ""),
        )
}

object MealFullPreviewData {
    val uiModel1 =
        MealUiModel(
            id = "1",
            name = "Meal 1",
            iconUrl = "https://upload.wikimedia.org/wikipedia/commons/0/0f/Android_phones.jpg",
            ingredients =
                listOf(
                    IngredientItemPreviewData.uiModel1,
                    IngredientItemPreviewData.uiModel2,
                    IngredientItemPreviewData.uiModel3,
                    IngredientItemPreviewData.uiModel4,
                    IngredientItemPreviewData.uiModel5,
                ),
            instructions =
                "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Vivamus malesuada, sem ut vulputate dictum, risus leo luctus nisl, et egestas ex libero quis erat. Donec bibendum volutpat eros, et auctor ex egestas sed. Proin neque enim, tempus id nisl nec, tempor luctus tortor. Duis ultricies, augue quis facilisis bibendum, leo dolor interdum ligula, in interdum tortor velit ut lectus. Morbi iaculis nec nunc luctus mollis. Vivamus ut lacus tincidunt, vestibulum arcu vitae, feugiat quam. Pellentesque feugiat bibendum mattis. Suspendisse potenti. Phasellus viverra nibh at mi efficitur, et facilisis quam egestas.\n" +
                    "\n" +
                    "Maecenas ultrices risus lectus, eget sagittis risus consequat volutpat. Maecenas ultricies faucibus interdum. Morbi ut neque egestas, condimentum mi eget, commodo ex. Proin efficitur rutrum lacus, eget molestie sapien rutrum eu. Integer consectetur leo ac leo pulvinar, congue vulputate lacus laoreet. Etiam nec justo at orci aliquam volutpat. Maecenas accumsan rhoncus purus quis rutrum. Aliquam pellentesque risus eu dui pretium eleifend. Proin non sapien imperdiet, congue mauris nec, suscipit ligula. Proin eu pellentesque sapien, a finibus nibh. Phasellus at ligula felis. Nulla non enim pellentesque, venenatis enim vitae, porta dui. Praesent sed urna mauris. Cras mattis nulla lorem, et pharetra lacus hendrerit eu. Maecenas venenatis interdum consectetur. Interdum et malesuada fames ac ante ipsum primis in faucibus.\n" +
                    "\n" +
                    "Mauris imperdiet lacinia tortor a porta. Sed in semper urna. Proin sed ex ut sem dictum dignissim vitae vitae sem. Praesent suscipit urna fringilla dui tincidunt facilisis. Pellentesque ac porttitor diam. Cras ornare placerat lectus, et tempus neque condimentum eu. Mauris malesuada consequat metus. Ut id sapien est. Nunc lac",
            model = MealModel("1", "Meal 1", ""),
        )
}

@OptIn(ExperimentalSharedTransitionApi::class)
@Preview
@Composable
private fun MealCardPreview() {
    val visible by remember {
        mutableStateOf(true)
    }

    AppTheme {
        SharedTransitionLayout {
            AnimatedVisibility(visible = visible) {
                MealCard(
                    animatedVisibilityScope = this@AnimatedVisibility,
                    sharedTransitionScope = this@SharedTransitionLayout,
                    uiModel = MealCardPreviewData.uiModel1,
                    events =
                        object : MealCardEvents {
                            override fun onMealCardClicked(uiModel: MealUiModel) = Unit
                        },
                )
            }
        }
    }
}
