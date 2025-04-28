package posttan.controllers.pizzaBox

import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class PizzaBoxMessage(val outcome: String, val messages: List<String>) {

    companion object {
        fun serialize(): String {
            return Json.encodeToString(this)
        }

        fun deserialize(message: String): PizzaBoxMessage {
            return Json.decodeFromString<PizzaBoxMessage>(message)
        }

        fun desrializeMessages(): ArrayList<PizzaBoxMessage> {
            val messagesAsString = PizzaBoxMessage::class.java.getResource("pizzaBox/messages.json")?.readText()

            return if (messagesAsString == null) {
                ArrayList()
            } else {
                Json.decodeFromString<ArrayList<PizzaBoxMessage>>(messagesAsString)
            }
        }
    }
}
