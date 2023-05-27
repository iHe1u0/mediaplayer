package cc.imorning.mediaplayer.utils.list

import android.content.Context
import android.provider.MediaStore
import android.support.v4.media.MediaMetadataCompat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MusicList {
    suspend fun getAllMusic(context: Context): List<MediaMetadataCompat> =
        withContext(Dispatchers.IO) {
            val result = mutableListOf<MediaMetadataCompat>()

            // 设定想要获取的列名/字段名称（需要用到MediaStore中定义的常量）
            val projection = arrayOf(
                MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST,
                MediaStore.Audio.Media.DURATION,
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

                    // TODO：构造一个mediaMetadata对象，将其加入到result列表中

                }

                cursor.close()
            }
            return@withContext result
        }

}