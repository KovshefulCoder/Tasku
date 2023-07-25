package ru.kovsh.tasku.models.areas

import android.content.SharedPreferences
import android.util.Log
import retrofit2.HttpException
import ru.kovsh.tasku.models.areas.entities.requestBodies.AddAreaRequest
import ru.kovsh.tasku.models.areas.entities.requestBodies.replaceAreasRequests.ReplaceAreasRequestBasic
import ru.kovsh.tasku.models.areas.entities.requestBodies.replaceAreasRequests.ReplaceAreasRequestFirst
import ru.kovsh.tasku.models.areas.entities.requestBodies.replaceAreasRequests.ReplaceAreasRequestLast
import ru.kovsh.tasku.models.areas.entities.resultStates.AddAreaResultState
import ru.kovsh.tasku.models.areas.entities.resultStates.GetAreasResultState
import ru.kovsh.tasku.models.areas.entities.resultStates.ReplaceAreasResultState
import ru.kovsh.tasku.models.auth.UnauthorizedException
import java.net.UnknownHostException

class AreasRepositoryImplementation(private val api: AreasAPI) :
    AreasRepository {

    override suspend fun getAreas(): GetAreasResultState {
        return try {
            val areas = api.getAreas()
            GetAreasResultState.Success(areas.areas_info)
        } catch (e: HttpException) {
            when (e.code()) {
                401 -> GetAreasResultState.Unauthorized
                500 -> GetAreasResultState.InternalServerError
                else -> {
                    Log.e("Tokens and getAreas", "AreasRepoImplementation getAreas: unexpected httpException " + e.code().toString())
                    GetAreasResultState.Error
                }
            }

        }
        catch (e: UnauthorizedException) {
            Log.e("Tokens and getAreas", "AreasRepoImplementation getAreas: UnauthorizedException :$e")
            throw UnauthorizedException("User is not authorized")
        }
        catch (e: UnknownHostException) {
            Log.e("Tokens and getAreas", "AreasRepoImplementation getAreas: UnknownHostException :$e")
            GetAreasResultState.NoServer
        }
        catch (e: Exception) {
            Log.e("Tokens and getAreas", "Exception in Implementation of getAreas:$e")
            GetAreasResultState.Error
        }
    }

    override suspend fun addArea(title: String): AddAreaResultState {
        return try {
            val newArea = api.addArea(request = AddAreaRequest(title=title))
            AddAreaResultState.Success(newArea.area_id)
        } catch (e: HttpException) {
            Log.e("addArea", "AreasRepoImplementation addArea: httpException " + e.code().toString())
            when (e.code()) {
                400 -> AddAreaResultState.BadRequest
                401 -> AddAreaResultState.Unauthorized
                403 -> { AddAreaResultState.Forbidden }
                500 -> AddAreaResultState.InternalServerError
                else -> {
                    Log.e("addArea", "AreasRepoImplementation addArea: unexpected httpException " + e.code().toString())
                    AddAreaResultState.Error
                }
            }
        }
        catch (e: UnauthorizedException) {
            Log.e("addArea", "AreasRepoImplementation addArea: UnauthorizedException :$e")
            throw UnauthorizedException("User is not authorized")
        }
        catch (e: Exception) {
            Log.e("addArea", "Exception in Implementation of addArea:$e")
            AddAreaResultState.Error
        }
    }

    override suspend fun replaceAreas(topID: Long, moveID: Long, bottomID: Long): ReplaceAreasResultState {
        return try {
            if (topID == (-1).toLong()) {
                api.replaceAreas(request = ReplaceAreasRequestBasic(bottomID, moveID))
                //api.replaceAreasFirst(request = ReplaceAreasRequestFirst(bottomID, moveID))
            } else if (bottomID == (-1).toLong()) {
                api.replaceAreas(request = ReplaceAreasRequestBasic(moveID, topID))
                //api.replaceAreasLast(request = ReplaceAreasRequestLast(moveID, topID))
            } else {
                api.replaceAreas(request = ReplaceAreasRequestBasic(bottomID, moveID, topID))
            }
            ReplaceAreasResultState.Success
        } catch (e: HttpException) {
            Log.e("replaceAreas", "AreasRepoImplementation replaceAreas: httpException " + e.code().toString())
            when (e.code()) {
                401 -> ReplaceAreasResultState.Unauthorized
                403 -> ReplaceAreasResultState.Forbidden
                500 -> ReplaceAreasResultState.InternalServerError
                else -> {
                    Log.e("replaceAreas", "AreasRepoImplementation replaceAreas: unexpected httpException " + e.code().toString())
                    ReplaceAreasResultState.Error
                }
            }

        }
        catch (e: UnauthorizedException) {
            Log.e("replaceAreas", "AreasRepoImplementation addArea: UnauthorizedException :$e")
            throw UnauthorizedException("User is not authorized")
        }
        catch (e: Exception) {
            Log.e("replaceAreas", "Exception in Implementation of replaceAreas:$e")
            ReplaceAreasResultState.Error
        }
    }


}