package com.briak.mealsmenu.data.repository.categories

import com.briak.mealsmenu.data.network.dto.CategoryDto
import com.briak.mealsmenu.domain.categories.CategoryModel

object CategoriesMapper {

    fun mapFromDto(dto: CategoryDto): CategoryModel =
        CategoryModel(
            id = dto.id,
            name = dto.name,
            iconUrl = dto.iconUrl,
            description = dto.description,
        )

}