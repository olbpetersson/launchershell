package se.olapetersson.launchersh.commands

import android.content.Context
import se.olapetersson.launchersh.events.Event

class HelpCommand: Command {

    val TRIGGER = "help"

    override fun getTrigger(): String {
        return TRIGGER
    }

    override fun execute(context: Context, rawInput: String): Event {
        val presentationText = "Try out by typing one of the following commands:\n\n${Commands.availableCommands.map { cmd ->
            cmd.getTrigger() + " - " + cmd.getHelpText()
        }.joinToString("\n")}"
        return Event(this, presentationText)
    }

    override fun getHelpText(): String {
        return "displays a helpful text"
    }

}