package se.olapetersson.launchersh.events

import se.olapetersson.launchersh.commands.Command

data class Event(val derivedFromCmd: Command, val presentationText: String)