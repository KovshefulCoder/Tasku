package ru.kovsh.tasku.models.areas

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import ru.kovsh.tasku.models.areas.entities.requestBodies.AddAreaRequest
import ru.kovsh.tasku.models.areas.entities.requestBodies.replaceAreasRequests.ReplaceAreasRequestBasic
import ru.kovsh.tasku.models.areas.entities.requestBodies.replaceAreasRequests.ReplaceAreasRequestFirst
import ru.kovsh.tasku.models.areas.entities.requestBodies.replaceAreasRequests.ReplaceAreasRequestLast
import ru.kovsh.tasku.models.areas.entities.resultBodies.AddAreaResultBody
import ru.kovsh.tasku.models.areas.entities.resultBodies.GetAreasResultBody

interface AreasAPI {
    @GET("area/get_areas")
    suspend fun getAreas(): GetAreasResultBody
    @POST("area/add_area")
    suspend fun addArea(@Body request: AddAreaRequest): AddAreaResultBody
    @POST("area/replace_area")
    suspend fun replaceAreas(@Body request: ReplaceAreasRequestBasic)
}
