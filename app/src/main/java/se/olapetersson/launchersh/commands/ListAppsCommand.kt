package se.olapetersson.launchersh.commands

import android.content.Context
import se.olapetersson.launchersh.commands.AppCommandHelper.getResolveInfos
import se.olapetersson.launchersh.commands.AppCommandHelper.resolveInfoToAppLabel
import se.olapetersson.launchersh.events.Event

class ListAppsCommand : Command {
    private val TRIGGER = "ls"

    override fun getTrigger(): String {
        return TRIGGER
    }

    override fun execute(context: Context, rawInput: String): Event {
        val appNames = getAppLabels(context)
        return Event(this, appNames.joinToString("\r\n"))
    }

    override fun getHelpText(): String {
        return "list all your installed applications"
    }

    override fun isValid(context: Context, rawInput: String): Boolean {
        return rawInput == TRIGGER
    }

    private fun getAppLabels(context: Context): List<String> {
        val packageManager = context.packageManager
        val apps = getResolveInfos(packageManager)!!.map { resolveInfo ->
            resolveInfoToAppLabel(resolveInfo, packageManager)
        }
        return apps
    }

}
