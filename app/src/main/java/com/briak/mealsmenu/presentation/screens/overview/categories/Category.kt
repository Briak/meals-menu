package com.briak.mealsmenu.presentation.screens.overview.categories

import androidx.compose.material3.FilterChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.briak.mealsmenu.domain.categories.CategoryModel
import com.briak.mealsmenu.presentation.theme.AppTheme

@Stable
data class CategoryUiModel(
    val id: String,
    val name: String,
    val iconUrl: String,
    val description: String,
    val selected: Boolean,
    val position: Int,
) {
    companion object {
        fun create(categoryModel: CategoryModel, selected: Boolean, position: Int) =
            CategoryUiModel(
                id = categoryModel.id,
                name = categoryModel.name,
                iconUrl = categoryModel.iconUrl,
                description = categoryModel.description,
                selected = selected,
                position = position,
            )
    }
}

interface CategoryCardEvents {
    fun onCategoryCardClicked(uiModel: CategoryUiModel)
}

@Composable
fun CategoryCard(
    modifier: Modifier = Modifier,
    uiModel: CategoryUiModel,
    events: CategoryCardEvents,
) {
    FilterChip(
        modifier = modifier,
        selected = uiModel.selected,
        onClick = { events.onCategoryCardClicked(uiModel) },
        label = { Text(text = uiModel.name) },
    )
}

object CategoryCardPreviewData {
    val uiModel1 = CategoryUiModel(
        id = "1",
        name = "Category 1",
        iconUrl = "https://upload.wikimedia.org/wikipedia/commons/0/0f/Android_phones.jpg",
        description = "Category 1 description",
        selected = true,
        position = 0,
    )
    val uiModel2 = CategoryUiModel(
        id = "2",
        name = "Category 2",
        iconUrl = "https://upload.wikimedia.org/wikipedia/commons/0/0f/Android_phones.jpg",
        description = "Category 2 description",
        selected = false,
        position = 1,
    )
}

@Preview
@Composable
private fun CategoryCardPreview() {
    AppTheme {
        CategoryCard(
            uiModel = CategoryCardPreviewData.uiModel1,
            events = object : CategoryCardEvents {
                override fun onCategoryCardClicked(uiModel: CategoryUiModel) = Unit
            },
        )
    }
}