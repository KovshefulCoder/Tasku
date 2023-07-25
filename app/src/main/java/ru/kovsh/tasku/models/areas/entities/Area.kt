package ru.kovsh.tasku.models.areas.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "areas")
data class Area(
    @PrimaryKey
    val area_id: Long,
    val title: String,
)