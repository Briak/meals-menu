package com.briak.mealsmenu.presentation.screens.mealdetails.ingredients

import android.graphics.Color
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.ui.tooling.preview.Preview
import com.briak.mealsmenu.domain.meals.IngredientModel
import com.briak.mealsmenu.presentation.theme.AppTheme

@Stable
data class IngredientUiModel(
    val name: String,
    val measure: String,
) {
    companion object {
        fun create(ingredientModel: IngredientModel) =
            IngredientUiModel(
                name = ingredientModel.name,
                measure = ingredientModel.measure,
            )
    }
}

@Composable
fun IngredientItem(uiModel: IngredientUiModel) {
    Row {
        Text(
            text = uiModel.name,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
        )
        Text(text = " ")
        Text(
            text = uiModel.measure,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onTertiaryContainer,
        )
    }
}

object IngredientItemPreviewData {
    val uiModel1 = IngredientUiModel(name = "Potatos", measure = "1.5 kg")
    val uiModel2 = IngredientUiModel(name = "Carotes", measure = "2")
    val uiModel3 = IngredientUiModel(name = "Butter", measure = "15 gr")
    val uiModel4 = IngredientUiModel(name = "Water", measure = "5 l")
    val uiModel5 = IngredientUiModel(name = "Salt", measure = "4 tsp")
}

@Preview(
    showBackground = true,
    backgroundColor = Color.WHITE.toLong(),
)
@Composable
private fun IngredientItemPreview() {
    AppTheme {
        IngredientItem(IngredientItemPreviewData.uiModel1)
    }
}
