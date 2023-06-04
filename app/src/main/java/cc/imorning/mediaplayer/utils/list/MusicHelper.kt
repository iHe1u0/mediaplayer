package cc.imorning.mediaplayer.utils.list

import android.annotation.SuppressLint
import android.content.Context
import android.provider.MediaStore
import cc.imorning.mediaplayer.data.MusicItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object MusicHelper {

    @SuppressLint("Range")
    suspend fun getAllMusic(context: Context): MutableList<MusicItem> =
        withContext(Dispatchers.IO) {
            val result = mutableListOf<MusicItem>()
            // 设定想要获取的列名/字段名称（需要用到MediaStore中定义的常量）
            val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
                MediaStore.Audio.Media.DATA
                /*其他所需的属性*/
            )

            val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"

            val cursor =
                context.contentResolver.query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, projection, selection, null, null)

            if (cursor != null) {

                while (cursor.moveToNext()) {
                    val id =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
                    val title =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                    val artist =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    val duration =
                        cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                    val path =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                    result.add(
                        MusicItem(
                            id = id,
                            name = title,
                            artists = artist,
                            duration = duration,
                            path = path
                        )
                    )
                }

                cursor.close()
            }
            return@withContext result
        }

    fun getMusicItem(context: Context, musicId: String): MusicItem? {
        if (musicId.toInt() < 0) {
            return null
        }
        // 设定想要获取的列名/字段名称（需要用到MediaStore中定义的常量）
        val projection = arrayOf(
            MediaStore.Audio.Media._ID,
            MediaStore.Audio.Media.TITLE,
            MediaStore.Audio.Media.ARTIST,
            MediaStore.Audio.Media.DURATION,
            MediaStore.Audio.Media.DATA
            /*其他所需的属性*/
        )

        val selection = "${MediaStore.Audio.Media.IS_MUSIC} != 0"

        val cursor =
            context.contentResolver.query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection,
                selection,
                null,
                null
            )

        if (cursor != null) {
            while (cursor.moveToNext()) {
                val id =
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID))
                if (id == musicId) {
                    val title =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE))
                    val artist =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST))
                    val duration =
                        cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media.DURATION))
                    val path =
                        cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))
                    cursor.close()
                    return MusicItem(
                        id = id,
                        name = title,
                        artists = artist,
                        duration = duration,
                        path = path
                    )
                }
            }
            cursor.close()
        }
        return null
    }
}