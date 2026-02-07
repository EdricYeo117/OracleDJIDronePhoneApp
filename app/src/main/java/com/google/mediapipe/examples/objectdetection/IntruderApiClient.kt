import android.util.Log
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.util.concurrent.TimeUnit

class IntruderApiClient(
    private val baseUrl: String,   // e.g. "http://192.168.1.50:8080"
    private val apiKey: String? = null
) {
    private val client = OkHttpClient.Builder()
        .connectTimeout(3, TimeUnit.SECONDS)
        .writeTimeout(3, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .build()

    private val jsonType = "application/json; charset=utf-8".toMediaType()

    fun sendIntruderEvent(jsonBody: String) {
        val url = "$baseUrl/v1/intrusion/events"

        val reqBuilder = Request.Builder()
            .url(url)
            .post(jsonBody.toRequestBody(jsonType))

        if (!apiKey.isNullOrBlank()) {
            reqBuilder.header("X-API-Key", apiKey)
        }

        val request = reqBuilder.build()

        // Async call (does not block camera thread)
        client.newCall(request).enqueue(object : okhttp3.Callback {
            override fun onFailure(call: okhttp3.Call, e: java.io.IOException) {
                Log.e("IntruderApiClient", "POST failed: ${e.message}", e)
            }

            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                response.use {
                    Log.d("IntruderApiClient", "POST status=${response.code} body=${response.body?.string()}")
                }
            }
        })
    }
}
