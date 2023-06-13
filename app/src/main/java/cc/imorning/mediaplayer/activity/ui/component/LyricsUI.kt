package cc.imorning.mediaplayer.activity.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

private const val TAG = "LyricsUI"

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LyricsUI(
    liveTime: Long = 0L,
    lyricsEntryList: String
) {
    var time = 0L
    // 解析歌词，并按照时间排序
    val entries = parseLyrics(lyricsEntryList).sortedBy { it.time }

    // 在所有歌词文本中，找到与当前时间对应的那句歌词
    val currentEntry = entries.findLast { it.time <= liveTime }
    val lazyListState = rememberLazyListState()
    val itemCount =
        remember { derivedStateOf { lazyListState.layoutInfo.visibleItemsInfo.count() } }
    LaunchedEffect(currentEntry) {
        val index = entries.indexOf(currentEntry) - itemCount.value / 2
        if (index >= 0) {
            lazyListState.animateScrollToItem(index)
        }
    }


    Column(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            state = lazyListState,
            // flingBehavior = rememberSnapFlingBehavior(lazyListState)
        ) {
            items(entries.size) { index ->
                if (entries[index] == currentEntry) {
                    Text(
                        text = entries[index].text,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.bodyLarge
                    )
                } else {
                    Text(
                        text = entries[index].text,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        textAlign = TextAlign.Center,
                        color = Color.Gray,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}

data class LyricEntry(val time: Long, val text: String)

private fun parseLyrics(rawText: String): List<LyricEntry> {
    return rawText.split("\n")
        .mapNotNull { entryText ->
            val matcher = Regex("\\[(\\d{2}):(\\d{2}).(\\d{2})]\\s*(.*)").find(entryText)
            with(matcher?.groupValues) {
                if (this != null && size >= 5) {
                    val time =
                        this[1].toLong() * 60_000 + this[2].toLong() * 1000 + this[3].toLong()
                    LyricEntry(time, this[4])
                } else {
                    null
                }
            }
        }
}
