package connectfour.api

// Browser EventSource for SSE — not in Kotlin/JS stdlib
external class EventSource(url: String) {
    var onerror: ((dynamic) -> Unit)?
    fun addEventListener(type: String, listener: (SseEvent) -> Unit)
    fun close()
}

external interface SseEvent {
    val data: String
}
