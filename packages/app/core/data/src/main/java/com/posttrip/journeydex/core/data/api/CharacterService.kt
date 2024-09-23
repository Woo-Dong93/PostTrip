package com.posttrip.journeydex.core.data.api

import com.posttrip.journeydex.core.data.model.request.CollectCharacter
import com.posttrip.journeydex.core.data.model.response.CharacterList
import com.posttrip.journeydex.core.data.model.travel.Character
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface CharacterService {
    @POST("/character/user")
    suspend fun collectCharacter(
        @Body body : CollectCharacter
    ) : Response<CollectCharacter>

    @GET("/character/{id}")
    suspend fun getCharacters(
        @Path("id") id : String
    ) : Response<CharacterList>
}
