package connectfour.api

// Browser EventSource — not in Kotlin/JS stdlib.
// listener is declared as (dynamic) to avoid Kotlin/JS IR wrapping issues
// with native JS MessageEvent objects.
external class EventSource(url: String) {
    var onerror: ((dynamic) -> Unit)?
    fun addEventListener(type: String, listener: (dynamic) -> Unit)
    fun close()
}
