package com.kaelesty.data.profile

import com.kaelesty.data.UrlConstants
import com.kaelesty.data.dtos.UserMetaResponse
import com.kaelesty.domain.memberProfile.MemberProfileRepository
import com.kaelesty.domain.memberProfile.getProfile.GetProfileBody
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

class MemberProfileRepoImpl(
    val httpClient: HttpClient
): MemberProfileRepository {


    override suspend fun getAvatarUrl(jwt: String): String? {
        try {
            val response = httpClient.get(UrlConstants.API_SECONDARY.url + "/getUserMeta") {
                parameter("jwt", "7")
                contentType(ContentType.Application.Json)
            }
            if (response.status != HttpStatusCode.OK) {
                return null
            }
            return response.body<UserMetaResponse>().githubAvatar
        }
        catch (e: Exception) {
            e
            return null
        }
    }

    override suspend fun getProjects(jwt: String): List<GetProfileBody.ProjectBody> {
        // TODO
        return listOf()
    }
}