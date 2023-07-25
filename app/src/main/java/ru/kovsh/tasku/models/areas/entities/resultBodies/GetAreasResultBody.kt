package ru.kovsh.tasku.models.areas.entities.resultBodies

import ru.kovsh.tasku.models.areas.entities.Area

data class GetAreasResultBody(
    val areas_info: List<Area>
)
