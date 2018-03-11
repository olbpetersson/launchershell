package se.olapetersson.launchersh.commands

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.util.Log
import se.olapetersson.launchersh.commands.AppCommandHelper.getResolveInfos
import se.olapetersson.launchersh.commands.AppCommandHelper.resolveInfoToAppLabel
import se.olapetersson.launchersh.events.Event

class LaunchAppCommand : Command {

    val TRIGGER = "./"

    override fun getTrigger(): String {
        return TRIGGER
    }

    override fun execute(context: Context, rawInput: String): Event {
        val appName = trimTrigger(rawInput)
        Log.i("LauncherShell", "Received ${rawInput} and will search for app ${appName}")
        if (appExists(appName, context.packageManager)) {
            launchApp(appName, context)
        }

        return Event(this, "${appName} launched")
    }

    override fun getHelpText(): String {
        return "opens an application by writing '${TRIGGER}<appName>'"
    }

    override fun isValid(context: Context, rawInput: String): Boolean {
        return appExists(trimTrigger(rawInput), context.packageManager)

    }

    private fun trimTrigger(rawInput: String): String {
        return rawInput.replace(TRIGGER, "")
    }

    private fun appExists(appName: String, packageManager: PackageManager): Boolean {
        return getLowerCasedAppLabelsWithoutSpace(packageManager, getResolveInfos(packageManager)).contains(appName)
    }

    private fun launchApp(appName: CharSequence, context: Context) {
        val packageManager = context.packageManager
        val resolveInfos = getResolveInfos(packageManager)

        val appCandidate= resolveInfos!!.filter { resolveInfo: ResolveInfo ->
            resolveInfoToAppLabel(resolveInfo, packageManager) == appName.toString()
        }

        if (appCandidate.size == 1) {
            var activityInfo = appCandidate.get(0).activityInfo
            launchIntent(activityInfo.packageName, activityInfo.name, context)
        } else {
            Log.i("LauncherShell", "Did not find a single candidate for" + appCandidate)
        }
    }

    private fun launchIntent(packageName: String, name: String, context: Context) {
        val launchIntent = Intent(Intent.ACTION_MAIN)
        launchIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        launchIntent.component = ComponentName(packageName, name)
        launchIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        context.startActivity(launchIntent)
    }


    private fun getLowerCasedAppLabelsWithoutSpace(packageManager: PackageManager, resolveInfos: MutableList<ResolveInfo>?): List<String> {
        return resolveInfos!!.map { resolveInfo -> resolveInfo.loadLabel(packageManager).toString().toLowerCase().replace(" ", "") }
    }

}