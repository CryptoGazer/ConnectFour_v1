package connectfour.api

import kotlinx.browser.window
import kotlinx.coroutines.await
import org.w3c.fetch.RequestInit
import org.w3c.fetch.Response

suspend fun apiGet(url: String): String {
    val resp: Response = window.fetch(url).await()
    check(resp.ok) { "HTTP ${resp.status}" }
    return resp.text().await()
}

suspend fun apiPost(url: String, body: String): String {
    val init: dynamic = js("({})")
    init.method = "POST"
    init.body = body
    init.headers = js("({'Content-Type': 'application/json'})")
    val resp: Response = window.fetch(url, init.unsafeCast<RequestInit>()).await()
    check(resp.ok) { "HTTP ${resp.status}" }
    return resp.text().await()
}
