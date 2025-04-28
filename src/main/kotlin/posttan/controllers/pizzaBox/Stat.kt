package posttan.controllers.pizzaBox

import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Stat(val userId: Long, val sliceCount: Int, val dayStreak: Int, val lastOpened: LocalDate) {

    companion object {
        fun deserializeStats(stats: String): ArrayList<Stat> {
            return Json.decodeFromString<ArrayList<Stat>>(stats)
        }

        fun serializeStats(stats: List<Stat>): String {
            return Json.encodeToString(stats)
        }
    }

    fun isRotting(): Boolean {
        return lastOpened.toJavaLocalDate().datesUntil(java.time.LocalDate.now()).count() > 2
    }
}
