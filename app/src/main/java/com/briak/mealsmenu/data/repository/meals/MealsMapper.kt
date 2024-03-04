package com.briak.mealsmenu.data.repository.meals

import com.briak.mealsmenu.data.network.dto.MealDto
import com.briak.mealsmenu.domain.meals.IngredientModel
import com.briak.mealsmenu.domain.meals.MealModel

object MealsMapper {

    fun mapMealFromDto(dto: MealDto): MealModel =
        MealModel(
            id = dto.id,
            name = dto.name,
            iconUrl = dto.iconUrl,
            categoryName = dto.categoryName,
            areaName = dto.areaName,
            instructions = dto.instructions,
            tags = dto.tags,
            youtubeUrl = dto.youtubeUrl,
            sourceUrl = dto.sourceUrl,
            ingredients = mapIngredientsFromDto(dto),
        )

    private fun mapIngredientsFromDto(dto: MealDto): List<IngredientModel> =
        buildList {
            mapIngredientFromDto(dto.ingredient1, dto.measure1)?.let { add(it) }
            mapIngredientFromDto(dto.ingredient2, dto.measure2)?.let { add(it) }
            mapIngredientFromDto(dto.ingredient3, dto.measure3)?.let { add(it) }
            mapIngredientFromDto(dto.ingredient4, dto.measure4)?.let { add(it) }
            mapIngredientFromDto(dto.ingredient5, dto.measure5)?.let { add(it) }
            mapIngredientFromDto(dto.ingredient6, dto.measure6)?.let { add(it) }
            mapIngredientFromDto(dto.ingredient7, dto.measure7)?.let { add(it) }
            mapIngredientFromDto(dto.ingredient8, dto.measure8)?.let { add(it) }
            mapIngredientFromDto(dto.ingredient9, dto.measure9)?.let { add(it) }
            mapIngredientFromDto(dto.ingredient10, dto.measure10)?.let { add(it) }
            mapIngredientFromDto(dto.ingredient11, dto.measure11)?.let { add(it) }
            mapIngredientFromDto(dto.ingredient12, dto.measure12)?.let { add(it) }
            mapIngredientFromDto(dto.ingredient13, dto.measure13)?.let { add(it) }
            mapIngredientFromDto(dto.ingredient14, dto.measure14)?.let { add(it) }
            mapIngredientFromDto(dto.ingredient15, dto.measure15)?.let { add(it) }
            mapIngredientFromDto(dto.ingredient16, dto.measure16)?.let { add(it) }
            mapIngredientFromDto(dto.ingredient17, dto.measure17)?.let { add(it) }
            mapIngredientFromDto(dto.ingredient18, dto.measure18)?.let { add(it) }
            mapIngredientFromDto(dto.ingredient19, dto.measure19)?.let { add(it) }
            mapIngredientFromDto(dto.ingredient20, dto.measure20)?.let { add(it) }
        }

    private fun mapIngredientFromDto(name: String?, measure: String?): IngredientModel? {
        if (name == null) return null
        return IngredientModel(name, measure ?: "")
    }

}