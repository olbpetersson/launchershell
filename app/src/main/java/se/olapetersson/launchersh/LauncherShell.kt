package se.olapetersson.launchersh

import android.app.Activity
import android.content.ComponentName
import android.content.Intent
import android.content.pm.ResolveInfo
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.EditText


class LauncherShell : Activity() {


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        listApps()
        setUpEditText()
    }

    fun setUpEditText() {
        val editText = findViewById<EditText>(R.id.input) as EditText
        editText.setOnKeyListener{ view, _, _ ->
            var affectedEditText = view as EditText
            val inputString = affectedEditText.text.trim().toString()
            Log.i("received", "wappapapap $inputString")
            if (inputString.startsWith("./")) {
                if(appExists(inputString.replace("./",""))) {
                    //TODO: Update to correct colors
                    affectedEditText.setTextColor(Color.GREEN)
                } else {
                    affectedEditText.setTextColor(Color.RED)
                }
            }
            true
        }


        editText.setOnEditorActionListener { view, actionId, event ->
            Log.i("EditText", "Listener was called with '$actionId'")
            val inputString = view.text.trim().toString()
            if (actionId.equals(EditorInfo.IME_ACTION_DONE)) {
                when {
                    inputString.startsWith("./") -> launchApp(inputString.replace("./", ""))
                    inputString.equals("ls") -> listApps()

                }
                Log.i("EditText", "The enter was inputed '$inputString'")
            } else {

            }
            view.text = null
            true
        }
    }

    fun appExists(inputString: String): Boolean {
        return getLowerCasedAppLabelsWithoutSpace(getResolveInfos()).contains(inputString)
    }

    fun launchApp(appName: CharSequence) {
        val resolveInfos = getResolveInfos()

        var appCandidate = resolveInfos!!.filter { rI ->
            rI.loadLabel(packageManager).toString().toLowerCase().equals(appName.toString())
        }

        if(appCandidate.size == 1) {
            var activityInfo = appCandidate.get(0).activityInfo
            launchIntent(activityInfo.packageName, activityInfo.name)
        } else {
            Log.i("Ambigous", "too many hits in " + appCandidate )
        }


    }

    fun launchIntent(packageName: String, name: String) {
        val launchIntent = Intent(Intent.ACTION_MAIN)
        launchIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        launchIntent.component = ComponentName(packageName, name)
        launchIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK

        applicationContext.startActivity(launchIntent)
    }

    fun listApps() {
        Log.i("", "-------------")
        val pgkAppsList = getResolveInfos()
        pgkAppsList!!.forEach({ apps ->
            Log.i("MainLauncher", apps.activityInfo.name);

        })
        Log.i("", "-------------")

    }

    fun getResolveInfos(): MutableList<ResolveInfo>? {
        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)
        return packageManager.queryIntentActivities(mainIntent, 0)
    }

    fun getLowerCasedAppLabelsWithoutSpace(resolveInfos: MutableList<ResolveInfo>?): List<String> {
        return resolveInfos!!.map { resolveInfo -> resolveInfo.loadLabel(packageManager).toString().toLowerCase().replace(" ","") }
    }
}
