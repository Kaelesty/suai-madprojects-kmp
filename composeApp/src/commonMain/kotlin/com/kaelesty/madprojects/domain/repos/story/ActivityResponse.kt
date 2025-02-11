package com.kaelesty.madprojects.domain.repos.story

import com.kaelesty.madprojects.domain.repos.profile.SharedProfile
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ActivityResponse(
    @SerialName("activities") val activities: List<Activity>,
    @SerialName("actors") val actors: Map<String, SharedProfile>,
)

@Serializable
data class Activity(
    @SerialName("type") val type: ActivityType,
    @SerialName("timeMillis") val timeMillis: Long,
    @SerialName("actorId") val actorId: String?,
    @SerialName("targetTitle") val targetTitle: String,
    @SerialName("targetId") val targetId: String?,
    @SerialName("secondaryTargetTitle") val secondaryTargetTitle: String?
)

@Serializable
enum class ActivityType {
    @SerialName("RepoBind") RepoBind,
    @SerialName("RepoUnbind") RepoUnbind,
    @SerialName("SprintStart") SprintStart,
    @SerialName("SprintFinish") SprintFinish,
    @SerialName("KardMove") KardMove,
    @SerialName("MemberAdd") MemberAdd,
    @SerialName("MemberRemove") MemberRemove,
}