package se.olapetersson.launchersh.commands

import android.content.Context
import android.util.Log
import se.olapetersson.launchersh.commands.AppCommandHelper.getResolveInfos
import se.olapetersson.launchersh.commands.AppCommandHelper.resolveInfoToAppLabel

class ListAppsCommand : Command {
    private val TRIGGER = "ls"

    override fun getTrigger(): String {
        return TRIGGER
    }

    override fun execute(context: Context, rawInput: String) {
        listApps(context)
    }

    fun listApps(context: Context) {
        Log.i("", "-------------")
        var packageManager = context.packageManager
        val pgkAppsList = getResolveInfos(packageManager)!!.map { resolveInfo ->
            resolveInfoToAppLabel(resolveInfo, packageManager)
        }
        pgkAppsList!!.forEach({ app ->
            Log.i("MainLauncher", app)

        })
        Log.i("", "-------------")

    }

}