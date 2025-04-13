data class Root(
    val count: Long,
    val filters: Map<String, Any>,
    val competitions: List<Competition>,
)

data class Competition(
    val id: Long,
    val area: Area,
    val name: String,
    val code: String?,
    val type: String,
    val emblem: String?,
    val plan: String?,
    val currentSeason: CurrentSeason?,
    val numberOfAvailableSeasons: Long,
    val lastUpdated: String,
)

data class Area(
    val id: Long,
    val name: String,
    val code: String,
    val flag: String?,
)

data class CurrentSeason(
    val id: Long,
    val startDate: String?,
    val endDate: String?,
    val currentMatchday: Long?,
    val winner: Winner?,
)

data class Winner(
    val id: Long,
    val name: String,
    val shortName: String?,
    val tla: String?,
    val crest: String?,
    val address: String,
    val website: String?,
    val founded: Long?,
    val clubColors: String?,
    val venue: String?,
    val lastUpdated: String,
)


data class AreaWithCompetitions(
    val name: String,
    val code: String,
    val competitions: List<Competition>
)

