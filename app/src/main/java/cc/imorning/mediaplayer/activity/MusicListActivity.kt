package cc.imorning.mediaplayer.activity

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import cc.imorning.mediaplayer.activity.ui.theme.MediaTheme
import cc.imorning.mediaplayer.utils.ui.ToastUtils
import cc.imorning.mediaplayer.viewmodel.MusicListViewModel

class MusicListActivity : BaseActivity() {

    private lateinit var viewModel: MusicListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this).get(MusicListViewModel::class.java)

        viewModel.update()

        setContent {
            MediaTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val context = LocalContext.current

                    val items = viewModel.musicItems.collectAsState()

                    LazyColumn {

                        item {
                            for (musicItem in items.value) {
                                MusicListItem(
                                    name = musicItem.name,
                                    artists = musicItem.artists,
                                    onClick = {
                                        ToastUtils.showMsg(context = context, musicItem.name)
                                    })
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MusicListItem(name: String, artists: String, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp)
            .background(MaterialTheme.colorScheme.background)
            .clickable {
                onClick()
            }
    ) {
        Text(
            text = name,
            fontSize = 24.sp
        )
        Text(
            text = artists,
            fontSize = 20.sp
        )
        Divider(Modifier.height(2.dp))
    }
}