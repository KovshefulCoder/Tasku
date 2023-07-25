package ru.kovsh.tasku.models.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import ru.kovsh.tasku.models.areas.entities.Area

@Dao
interface AreaDao {

    @Upsert
    suspend fun insertAreas(areas: List<Area>)

    @Insert
    suspend fun insert(area: Area)

    @Query("SELECT * FROM areas")
    suspend fun getAllAreas() : List<Area>
}