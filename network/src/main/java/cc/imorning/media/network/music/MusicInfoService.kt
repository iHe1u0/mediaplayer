package cc.imorning.media.network.music

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.QueryMap

interface MusicInfoService {

    /**
     * get music info by netease api
     */
    @Headers(
        "Accept: */*",
        "User-Agent:Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/114.0.0.0 Safari/537.36",
    )
    @GET("/api/search/pc")
    fun getMusicInfo(@QueryMap params: HashMap<String, String>): Call<NeteaseMusicInfoApi>

    /**
     * get music lyric by netease api
     */
    @GET("/api/song/lyric")
    fun getLyric(@QueryMap params: HashMap<String, String>): Call<LyricApi>
}