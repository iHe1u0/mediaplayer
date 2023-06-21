package cc.imorning.media.network.base

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServiceCreator {
    private const val NETEASE_MUSIC_HOST = "https://music.163.com"

    private val neteaseRetrofit = Retrofit.Builder()
        .baseUrl(NETEASE_MUSIC_HOST)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun <T> createNeteaseService(serviceClass: Class<T>): T = neteaseRetrofit.create(serviceClass)
}