package se.olapetersson.launchersh.events

import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import se.olapetersson.launchersh.R

class EventAdapter(val events: MutableList<Event> = ArrayList()): RecyclerView.Adapter<EventAdapter.ViewHolder>()  {
    override fun getItemCount(): Int {
        return events.size
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.layout_entered_command, parent, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        Log.i("LauncherShell", "Binding viewHolder for position ${position}\nWith event ${events[position]}")
        holder?.enteredCommand?.text = "> " +events[position].derivedFromCmd.getTrigger()
        holder?.presentationText?.text = events[position].presentationText
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val enteredCommand = itemView.findViewById<TextView>(R.id.enteredCommandCrocGap)!!
        val presentationText = itemView.findViewById<TextView>(R.id.commandPresentationText)!!

    }
}