package se.olapetersson.launchersh.commands

import android.content.Context

interface Command {

    fun getTrigger(): String
    fun execute(context: Context, rawInput: String)
}