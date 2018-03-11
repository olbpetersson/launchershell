package se.olapetersson.launchersh.commands

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo

object AppCommandHelper {

    inline fun getResolveInfos(packageManager: PackageManager): MutableList<ResolveInfo>? {
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        return packageManager.queryIntentActivities(mainIntent, 0)
    }

    inline fun resolveInfoToAppLabel(resolveInfo: ResolveInfo, packageManager: PackageManager): String {
        return resolveInfo.loadLabel(packageManager).toString().toLowerCase()
    }
}