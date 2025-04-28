package posttan.extensions

import dev.kordex.core.commands.Arguments
import dev.kordex.core.commands.application.slash.ephemeralSubCommand
import dev.kordex.core.commands.application.slash.group
import dev.kordex.core.commands.application.slash.publicSubCommand
import dev.kordex.core.commands.converters.impl.defaultingString
import dev.kordex.core.commands.converters.impl.user
import dev.kordex.core.components.components
import dev.kordex.core.components.publicButton
import dev.kordex.core.extensions.Extension
import dev.kordex.core.extensions.publicSlashCommand
import dev.kordex.core.i18n.withContext
import posttan.TEST_SERVER_ID
import posttan.controllers.pizzaBox.PizzaBox
import posttan.i18n.Translations

class SlashCommandExtension : Extension() {
	override val name = "test"

	override suspend fun setup() {

        //Commands for PizzaBox
        publicSlashCommand() {
            name = Translations.Commands.PizzaBox.name
            description = Translations.Commands.PizzaBox.description

            guild(TEST_SERVER_ID)

            group(Translations.Arguments.PizzaGroup.name) {

                //Subcommand to Open a Box
                publicSubCommand() {
                    name = Translations.Subcommands.Open.name
                    description = Translations.Subcommands.Open.description

                    action {
                        val resultMessage = PizzaBox.openBox(getMember())

                        respond {
                            //TODO: replace with amount of slices gained or lost
                            content = "You opened the box and got a number of slices..."
                        }
                    }
                }

                //Subcommand to see current slices
                ephemeralSubCommand() {
                    name = Translations.Subcommands.MySlices.name
                    description = Translations.Subcommands.MySlices.description

                    action {

                        respond {
                            //TODO: lookup user's slice count and print out
                            content = "You have an amount of slices..."
                        }
                    }
                }
            }
        }

		publicSlashCommand(::SlapSlashArgs) {
			name = Translations.Commands.Slap.name
			description = Translations.Commands.Slap.description

			guild(TEST_SERVER_ID)  // Otherwise it will take up to an hour to update

			action {
				// Don't slap ourselves on request, slap the requester!
				val realTarget = if (arguments.target.id == event.kord.selfId) {
					member
				} else {
					arguments.target
				}

				respond {
					content = Translations.Commands.Slap.response
						.withContext(this@action)
						.translateNamed(
							"target" to realTarget?.mention,
							"weapon" to arguments.weapon
						)
				}
			}
		}

		publicSlashCommand {
			name = Translations.Commands.Button.name
			description = Translations.Commands.Button.description

            guild(TEST_SERVER_ID)

			action {
				respond {
					components {
						publicButton {
							label = Translations.Components.Button.label
								.withLocale(this@action.getLocale())

							action {
								respond {
									content = Translations.Components.Button.response
										.withLocale(getLocale())
										.translate()
								}
							}
						}
					}
				}
			}
		}
	}

//    inner class PizzaBoxArgs : Arguments() {
//        val callingUser by user {
//            name
//        }
//    }

	inner class SlapSlashArgs : Arguments() {
		val target by user {
			name = Translations.Arguments.Target.name
			description = Translations.Arguments.Target.description
		}

		// Slash commands don't support coalescing strings
		val weapon by defaultingString {
			name = Translations.Arguments.Weapon.name

			defaultValue = "🐟"
			description = Translations.Arguments.Weapon.description
		}
	}
}
