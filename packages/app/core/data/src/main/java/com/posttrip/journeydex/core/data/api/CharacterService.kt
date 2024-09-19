package com.posttrip.journeydex.core.data.api

import com.posttrip.journeydex.core.data.model.request.CollectCharacter
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface CharacterService {
    @POST("/character/user")
    suspend fun collectCharacter(
        @Body body : CollectCharacter
    ) : Response<CollectCharacter>
}
