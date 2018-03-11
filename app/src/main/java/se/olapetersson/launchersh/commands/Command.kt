package se.olapetersson.launchersh.commands

import android.content.Context
import se.olapetersson.launchersh.events.Event

interface Command {

    fun getTrigger(): String
    fun execute(context: Context, rawInput: String): Event
    fun getHelpText(): String
    fun isValid(context: Context, rawInput: String): Boolean {
        return rawInput.startsWith(getTrigger())
    }
}