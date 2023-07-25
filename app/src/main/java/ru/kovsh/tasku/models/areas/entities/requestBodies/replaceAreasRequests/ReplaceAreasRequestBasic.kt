package ru.kovsh.tasku.models.areas.entities.requestBodies.replaceAreasRequests

data class ReplaceAreasRequestBasic(
    val bottom_area_id: Long? = null,
    val move_area_id: Long,
    val top_area_id: Long? = null
)
