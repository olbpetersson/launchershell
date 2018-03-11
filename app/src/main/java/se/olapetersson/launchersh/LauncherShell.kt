package se.olapetersson.launchersh

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import se.olapetersson.launchersh.commands.LaunchAppCommand
import se.olapetersson.launchersh.commands.ListAppsCommand


class LauncherShell : Activity() {
    //Check is this is possible to do with DI/dagger
    private val availableCommands = listOf(ListAppsCommand(), LaunchAppCommand())

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // listApps()
        setUpEditText()
    }

    fun setUpEditText() {
        val editText = findViewById<EditText>(R.id.input) as EditText
        /*  editText.setOnKeyListener{ view, _, _ ->
              var affectedEditText = view as EditText
              val inputString = affectedEditText.text.trim().toString()
              Log.i"LaunherShell, "wappapapap $inputString")
              if (inputString.startsWith("./")) {
                  if(appExists(inputString.replace("./",""))) {
                      //TODO: Update to correct colors
                      affectedEditText.setTextColor(Color.GREEN)
                  } else {
                      affectedEditText.setTextColor(Color.RED)
                  }
              }
              true
          }*/


        editText.setOnEditorActionListener { view, actionId, event ->
            Log.i("LaunherShell", "Listener was called with '$actionId'")
            val inputString = view.text.trim().toString()
            if (actionId.equals(EditorInfo.IME_ACTION_DONE)) {
                availableCommands.forEach { cmd ->
                    Log.i("LaunherShell", "${cmd.getTrigger()} is one of the triggers")
                    if (inputString.startsWith(cmd.getTrigger())) {
                        Log.i("LaunherShell", "calling ${cmd.getTrigger()}")
                        cmd.execute(this, inputString)
                    }
                }
                /*when {
                    inputString.startsWith("./") -> launchApp(inputString.replace("./", ""))
                    inputString.equals("ls") -> listApps()

                }*/
                Log.i("LaunherShell", "The raw string after enter: '$inputString'")
            }
            view.text = null
            true
        }
    }

}
