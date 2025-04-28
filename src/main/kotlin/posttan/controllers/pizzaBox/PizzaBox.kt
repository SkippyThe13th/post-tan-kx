package posttan.controllers.pizzaBox

import dev.kord.core.behavior.MemberBehavior
import java.io.File

object PizzaBox {

    var statMap = loadStats()
    var messageMap = loadMessages()

    fun openBox(user: MemberBehavior): String {
        var resultMessage = "You opened a pizza box!  Nice job!"

        return resultMessage
    }

    private fun loadStats(): Map<Long, Stat> {
        val stats = Stat.deserializeStats(File("resources/stats.json").readText())

        return stats.associateBy({it.userId}, {it})
    }

    fun saveStats() {
        val stats = Stat.serializeStats(statMap.values.toList())
        File("resources/stats.json").printWriter().print(stats)
    }

    private fun loadMessages(): Map<String, List<String>> {
        val messages = PizzaBoxMessage.desrializeMessages()

        return messages.associateBy({it.outcome}, {it.messages})
    }

}
