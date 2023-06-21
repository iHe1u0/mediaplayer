package cc.imorning.media.network.base

import cc.imorning.media.common.utils.LogUtils
import cc.imorning.media.network.music.MusicInfoService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

class NeteaseMusicHelper {

    companion object {
        private const val TAG = "NeteaseMusicHelper"
    }

    suspend fun getMusicId(musicName: String) {
        try {
            val params = HashMap<String, String>()
            params["s"] = musicName
            params["limit"] = "10"
            params["type"] = "1"
            val data =
                ServiceCreator.createNeteaseService(MusicInfoService::class.java)
                    .getMusicInfo(params).await()
        } catch (e: Exception) {
            LogUtils.e(TAG, e.localizedMessage!!, e)
        }
    }

    suspend fun <T> Call<T>.await(): T {
        return suspendCoroutine { continuation ->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body = response.body()
                    if (null != body) {
                        continuation.resume(body)
                    } else {
                        continuation.resumeWithException(
                            RuntimeException()
                        )
                    }
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    continuation.resumeWithException(t)
                }

            })
        }
    }
}