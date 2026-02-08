import android.util.Log
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

class IntruderApiClient(
    private val baseUrl: String,   // e.g. "http://192.168.0.5:8080"
    private val apiKey: String? = null
) {
    private val client = OkHttpClient.Builder()
        .addNetworkInterceptor { chain ->
            val req = chain.request()
            Log.d("IntruderApiClient", "OUTGOING HEADERS ->\n" + req.headers.toString())
            chain.proceed(req)
        }
        .connectTimeout(3, TimeUnit.SECONDS)
        .writeTimeout(3, TimeUnit.SECONDS)
        .readTimeout(5, TimeUnit.SECONDS)
        .build()

    private val jsonType = "application/json; charset=utf-8".toMediaType()

    private fun mask(k: String?): String {
        val s = k?.trim()
        if (s.isNullOrEmpty()) return "None"
        val n = s.length
        return if (n <= 8) "${s.take(2)}***${s.takeLast(2)}(len=$n)"
        else "${s.take(4)}***${s.takeLast(4)}(len=$n)"
    }

    fun sendIntruderEvent(jsonBody: String) {
        val safeBase = baseUrl.trim().trimEnd('/')
        val url = "$safeBase/v1/intrusion/events"

        val key = apiKey?.trim()

        Log.d("IntruderApiClient", "POST -> $url")
        Log.d("IntruderApiClient", "X-API-Key -> ${mask(key)}")
        Log.d("IntruderApiClient", "BODY -> $jsonBody")

        val reqBuilder = Request.Builder()
            .url(url)
            .post(jsonBody.toRequestBody(jsonType))
            .addHeader("Content-Type", "application/json")
            .addHeader("User-Agent", "IntruderAndroid/1.0")

        if (!key.isNullOrEmpty()) {
            reqBuilder.addHeader("X-API-Key", key)
        }

        client.newCall(reqBuilder.build()).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("IntruderApiClient", "POST failed: ${e.message}", e)
            }

            override fun onResponse(call: Call, response: Response) {
                response.use {
                    val bodyStr = it.body?.string()
                    if (!it.isSuccessful) {
                        Log.e("IntruderApiClient", "POST error=${it.code} body=$bodyStr")
                    } else {
                        Log.d("IntruderApiClient", "POST ok=${it.code} body=$bodyStr")
                    }
                }
            }
        })
    }
}
