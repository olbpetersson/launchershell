package se.olapetersson.launchersh

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.LinearLayout
import se.olapetersson.launchersh.commands.Commands
import se.olapetersson.launchersh.events.Event
import se.olapetersson.launchersh.events.EventAdapter


class LauncherShell : Activity() {
    //Check is this is possible to do with DI/dagger
    private val availableCommands = Commands.availableCommands
    private lateinit var eventAdapter: EventAdapter
    private lateinit var eventeRecyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setupRecyclerView()
        setUpEditText()
    }

    private fun setUpEditText() {
        val editText = findViewById<EditText>(R.id.input) as EditText
        editText.setOnKeyListener { view, _, _ ->
            val affectedEditText = view as EditText
            val inputString = affectedEditText.text.trim().toString()
            val foundAValidCommand = availableCommands.any{ cmd -> cmd.isValid(this, inputString) }
            affectedEditText.setTextColor(if(foundAValidCommand) Color.GREEN else Color.RED)

            true
        }

        editText.setOnEditorActionListener { view, actionId, event ->
            val inputString = view.text.trim().toString()
            if (actionId.equals(EditorInfo.IME_ACTION_DONE)) {
                availableCommands.forEach { cmd ->
                    when {
                        inputString.startsWith(cmd.getTrigger()) -> {
                            val event = cmd.execute(this, inputString)
                            Log.i("LauncherShell", "${event.presentationText} " +
                                    "added to events. Current size: ${eventAdapter.events.size}")
                            addEventToRecyclerView(event)
                        }
                    }
                }
                Log.d("LauncherShell", "The raw string after enter was hit: '$inputString'")
            }
            view.text = null
            true
        }
    }

    private fun setupRecyclerView() {
        eventeRecyclerView = findViewById<RecyclerView>(R.id.appListRecyclerView)
        eventeRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayout.VERTICAL, false)

        eventAdapter = EventAdapter()

        eventeRecyclerView.adapter = eventAdapter
    }

    private fun addEventToRecyclerView(event: Event) {
        eventAdapter.events.add(event)
        eventAdapter.notifyDataSetChanged()
        eventeRecyclerView.smoothScrollToPosition(eventAdapter.events.size-1)
    }

}
