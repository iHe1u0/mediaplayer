package cc.imorning.mediaplayer.data
import com.google.gson.annotations.SerializedName


data class NeteaseMusicInfoApi(
    @SerializedName("code")
    val code: Int = 0, // 200
    @SerializedName("result")
    val result: Result = Result()
)

data class Result(
    @SerializedName("songCount")
    val songCount: Int = 0, // 300
    @SerializedName("songs")
    val songs: List<Song> = listOf()
)

data class Song(
    @SerializedName("album")
    val album: Album = Album(),
    @SerializedName("alias")
    val alias: List<Any> = listOf(),
    @SerializedName("artists")
    val artists: List<ArtistXX> = listOf(),
    @SerializedName("audition")
    val audition: Any? = Any(), // null
    @SerializedName("bMusic")
    val bMusic: BMusic = BMusic(),
    @SerializedName("commentThreadId")
    val commentThreadId: String = "", // R_SO_4_36990266
    @SerializedName("copyFrom")
    val copyFrom: String = "",
    @SerializedName("copyright")
    val copyright: Int = 0, // 1
    @SerializedName("copyrightId")
    val copyrightId: Int = 0, // 7001
    @SerializedName("crbt")
    val crbt: Any? = Any(), // null
    @SerializedName("dayPlays")
    val dayPlays: Int = 0, // 0
    @SerializedName("disc")
    val disc: String = "", // 1
    @SerializedName("duration")
    val duration: Int = 0, // 212688
    @SerializedName("fee")
    val fee: Int = 0, // 1
    @SerializedName("ftype")
    val ftype: Int = 0, // 0
    @SerializedName("hMusic")
    val hMusic: HMusic = HMusic(),
    @SerializedName("hearTime")
    val hearTime: Int = 0, // 0
    @SerializedName("id")
    val id: Int = 0, // 36990266
    @SerializedName("lMusic")
    val lMusic: LMusic = LMusic(),
    @SerializedName("mMusic")
    val mMusic: MMusic = MMusic(),
    @SerializedName("mp3Url")
    val mp3Url: String = "", // http://m2.music.126.net/hmZoNQaqzZALvVp0rE7faA==/0.mp3
    @SerializedName("mvid")
    val mvid: Int = 0, // 524116
    @SerializedName("name")
    val name: String = "", // Faded
    @SerializedName("no")
    val no: Int = 0, // 1
    @SerializedName("playedNum")
    val playedNum: Int = 0, // 0
    @SerializedName("popularity")
    val popularity: Double = 0.0, // 100.0
    @SerializedName("position")
    val position: Int = 0, // 1
    @SerializedName("ringtone")
    val ringtone: Any? = Any(), // null
    @SerializedName("rtUrl")
    val rtUrl: Any? = Any(), // null
    @SerializedName("rtUrls")
    val rtUrls: List<Any> = listOf(),
    @SerializedName("rtype")
    val rtype: Int = 0, // 0
    @SerializedName("rurl")
    val rurl: Any? = Any(), // null
    @SerializedName("score")
    val score: Int = 0, // 100
    @SerializedName("starred")
    val starred: Boolean = false, // false
    @SerializedName("starredNum")
    val starredNum: Int = 0, // 0
    @SerializedName("status")
    val status: Int = 0 // 0
)

data class Album(
    @SerializedName("alias")
    val alias: List<Any> = listOf(),
    @SerializedName("artist")
    val artist: Artist = Artist(),
    @SerializedName("artists")
    val artists: List<ArtistXX> = listOf(),
    @SerializedName("blurPicUrl")
    val blurPicUrl: String = "", // http://p1.music.126.net/OEZ0FzlPU-GPUjqHgCRqjA==/109951165976856263.jpg
    @SerializedName("briefDesc")
    val briefDesc: String = "",
    @SerializedName("commentThreadId")
    val commentThreadId: String = "", // R_AL_3_3406843
    @SerializedName("company")
    val company: String = "", // 索尼音乐
    @SerializedName("companyId")
    val companyId: Int = 0, // 0
    @SerializedName("copyrightId")
    val copyrightId: Int = 0, // 7001
    @SerializedName("description")
    val description: String = "",
    @SerializedName("id")
    val id: Int = 0, // 3406843
    @SerializedName("idStr")
    val idStr: String = "", // 3406843
    @SerializedName("name")
    val name: String = "", // Faded
    @SerializedName("pic")
    val pic: Long = 0, // 109951165976856263
    @SerializedName("picId")
    val picId: Long = 0, // 109951165976856263
    @SerializedName("picId_str")
    val picIdStr: String = "", // 109951165976856263
    @SerializedName("picUrl")
    val picUrl: String = "", // http://p1.music.126.net/OEZ0FzlPU-GPUjqHgCRqjA==/109951165976856263.jpg
    @SerializedName("publishTime")
    val publishTime: Long = 0, // 1449158400000
    @SerializedName("size")
    val size: Int = 0, // 4
    @SerializedName("songs")
    val songs: List<Any> = listOf(),
    @SerializedName("status")
    val status: Int = 0, // 1
    @SerializedName("tags")
    val tags: String = "",
    @SerializedName("type")
    val type: String = "" // Single
)

data class ArtistXX(
    @SerializedName("albumSize")
    val albumSize: Int = 0, // 0
    @SerializedName("alias")
    val alias: List<Any> = listOf(),
    @SerializedName("briefDesc")
    val briefDesc: String = "",
    @SerializedName("id")
    val id: Int = 0, // 1045123
    @SerializedName("img1v1Id")
    val img1v1Id: Int = 0, // 0
    @SerializedName("img1v1Url")
    val img1v1Url: String = "", // http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg
    @SerializedName("musicSize")
    val musicSize: Int = 0, // 0
    @SerializedName("name")
    val name: String = "", // Alan Walker
    @SerializedName("picId")
    val picId: Int = 0, // 0
    @SerializedName("picUrl")
    val picUrl: String = "", // http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg
    @SerializedName("trans")
    val trans: String = ""
)

data class BMusic(
    @SerializedName("bitrate")
    val bitrate: Int = 0, // 128000
    @SerializedName("dfsId")
    val dfsId: Int = 0, // 0
    @SerializedName("extension")
    val extension: String = "", // mp3
    @SerializedName("id")
    val id: Long = 0, // 7238159975
    @SerializedName("name")
    val name: Any? = Any(), // null
    @SerializedName("playTime")
    val playTime: Int = 0, // 212688
    @SerializedName("size")
    val size: Int = 0, // 3403067
    @SerializedName("sr")
    val sr: Int = 0, // 44100
    @SerializedName("volumeDelta")
    val volumeDelta: Double = 0.0 // -58643.0
)

data class HMusic(
    @SerializedName("bitrate")
    val bitrate: Int = 0, // 320000
    @SerializedName("dfsId")
    val dfsId: Int = 0, // 0
    @SerializedName("extension")
    val extension: String = "", // mp3
    @SerializedName("id")
    val id: Long = 0, // 7238159977
    @SerializedName("name")
    val name: Any? = Any(), // null
    @SerializedName("playTime")
    val playTime: Int = 0, // 212688
    @SerializedName("size")
    val size: Int = 0, // 8507603
    @SerializedName("sr")
    val sr: Int = 0, // 44100
    @SerializedName("volumeDelta")
    val volumeDelta: Double = 0.0 // -62343.0
)

data class LMusic(
    @SerializedName("bitrate")
    val bitrate: Int = 0, // 128000
    @SerializedName("dfsId")
    val dfsId: Int = 0, // 0
    @SerializedName("extension")
    val extension: String = "", // mp3
    @SerializedName("id")
    val id: Long = 0, // 7238159975
    @SerializedName("name")
    val name: Any? = Any(), // null
    @SerializedName("playTime")
    val playTime: Int = 0, // 212688
    @SerializedName("size")
    val size: Int = 0, // 3403067
    @SerializedName("sr")
    val sr: Int = 0, // 44100
    @SerializedName("volumeDelta")
    val volumeDelta: Double = 0.0 // -58643.0
)

data class MMusic(
    @SerializedName("bitrate")
    val bitrate: Int = 0, // 192000
    @SerializedName("dfsId")
    val dfsId: Int = 0, // 0
    @SerializedName("extension")
    val extension: String = "", // mp3
    @SerializedName("id")
    val id: Long = 0, // 7238159978
    @SerializedName("name")
    val name: Any? = Any(), // null
    @SerializedName("playTime")
    val playTime: Int = 0, // 212688
    @SerializedName("size")
    val size: Int = 0, // 5104579
    @SerializedName("sr")
    val sr: Int = 0, // 44100
    @SerializedName("volumeDelta")
    val volumeDelta: Double = 0.0 // -60009.0
)

data class Artist(
    @SerializedName("albumSize")
    val albumSize: Int = 0, // 0
    @SerializedName("alias")
    val alias: List<Any> = listOf(),
    @SerializedName("briefDesc")
    val briefDesc: String = "",
    @SerializedName("id")
    val id: Int = 0, // 0
    @SerializedName("img1v1Id")
    val img1v1Id: Int = 0, // 0
    @SerializedName("img1v1Url")
    val img1v1Url: String = "", // http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg
    @SerializedName("musicSize")
    val musicSize: Int = 0, // 0
    @SerializedName("name")
    val name: String = "",
    @SerializedName("picId")
    val picId: Int = 0, // 0
    @SerializedName("picUrl")
    val picUrl: String = "", // http://p1.music.126.net/6y-UleORITEDbvrOLV0Q8A==/5639395138885805.jpg
    @SerializedName("trans")
    val trans: String = ""
)