package cc.imorning.media.network.music

import okhttp3.Cookie
import okhttp3.CookieJar
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.converter.gson.GsonConverterFactory

private const val TAG = "MusicInfoHelper"

object MusicInfoHelper {

    private const val baseUrl = "https://music.163.com"

    /**
     * we will retry to request if count<3
     */
    private var count = 0

    private val okHttpClientBuilder = HttpClient.client

    private object HttpClient {
        val client: OkHttpClient.Builder = OkHttpClient.Builder().cookieJar(NeteaseCookier())
    }

    private class NeteaseCookier : CookieJar {

        private val cookies = mutableListOf<Cookie>()

        override fun saveFromResponse(url: HttpUrl, cookieList: List<Cookie>) {
            cookies.clear()
            cookies.addAll(cookieList)
        }

        override fun loadForRequest(url: HttpUrl): List<Cookie> {
            return cookies
        }
    }

    private var _id: Int = -1
    private var lyric: String? = null

    suspend fun getLyricValue(id: Int): String? {
        getLyric(id)
        return runCatching {
            val startTime = System.currentTimeMillis()
            while (lyric.isNullOrEmpty()) {
                if (System.currentTimeMillis() - startTime > 10000) {
                    break
                }
            }
            _id = -1
            lyric
        }.getOrElse {
            null
        }
    }

    suspend fun getIdValue(name: String): Int {
        if (name.isEmpty()) {
            return -1
        }
        lyric = null
        getMusicInfo(name)
        return runCatching {
            val startTime = System.currentTimeMillis()
            while (_id < 0) {
                if (System.currentTimeMillis() - startTime > 10000) {
                    break
                }
            }
            _id
        }.getOrElse {
            -1
        }
    }

    private fun getLyric(id: Int): Unit {
        val retrofit = retrofit2.Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val params = HashMap<String, String>()
        params["os"] = "pc"
        params["id"] = id.toString()
        params["lv"] = "-1"
        params["kv"] = "-1"
        params["tv"] = "-1"
        val call = retrofit.create(MusicInfoService::class.java).getLyric(params)
        call.enqueue(object : Callback<LyricApi> {
            override fun onResponse(call: Call<LyricApi>, response: Response<LyricApi>) {
                lyric = response.body()?.lrc?.lyric
            }

            override fun onFailure(call: Call<LyricApi>, t: Throwable) {
                println(t.message)
            }
        })
    }

    private fun getMusicInfo(name: String) {
        val retrofit = retrofit2.Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClientBuilder.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val params = HashMap<String, String>()
        params["s"] = name
        params["limit"] = "10"
        params["type"] = "1"
        val call = retrofit.create(MusicInfoService::class.java).getMusicInfo(params)
        call.enqueue(object : Callback<NeteaseMusicInfoApi> {
            override fun onResponse(
                call: Call<NeteaseMusicInfoApi>,
                response: Response<NeteaseMusicInfoApi>
            ) {
                if (response.isSuccessful) {
                    val musicInfo = response.body()
                    if (musicInfo != null) {
                        when (musicInfo.code) {
                            200 -> {
                                _id = musicInfo.result.songs[0].id
                                getLyric(_id)
                            }

                            else -> {
                                if (count++ < 3) {
                                    return getMusicInfo(name)
                                }
                            }
                        }
                    }
                }
            }

            override fun onFailure(call: Call<NeteaseMusicInfoApi>, t: Throwable) {
                t.printStackTrace()
            }
        })

    }

}