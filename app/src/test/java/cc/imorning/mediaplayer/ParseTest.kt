package cc.imorning.mediaplayer

import org.junit.Test
import java.util.regex.Pattern

class ParseTest {
    @Test
    fun main() {
        val text = """
            [00:00.000] 作词 : royster lee
            [00:01.000] 作曲 : royster lee
            [00:02.000] 编曲 : royster lee
            [00:03.699]编曲:royster lee:
            [00:07.197]混录:royster lee
            [00:10.698]和声:royster lee
            [00:14.198]它说
            [00:15.1919]它说如果
            [00:17.198]有天路过
            [00:18.948]一片沙漠
            [00:20.699]遇到一个
            [00:22.4191]金发的小王子
            [24.697]请记得告诉我
            """.trimIndent()
        ParserUtils.parseLyric(text)
    }
}

internal object ParserUtils {
    fun parseLyric(lyric: String?) {

        // 定义匹配时间戳+文本内容的正则表达式
        val regex = "\\[(\\d{0,4}:?\\d{0,2}\\.\\d{0,4})]\\s*(.*)"
        val pattern = Pattern.compile(regex)
        val matcher = pattern.matcher(lyric)
        while (matcher.find()) {
            val timestamp = matcher.group(1) // 获取时间戳
            val content = matcher.group(2) // 获取文本内容并去除前后空格
            println("[$timestamp] $content") // 将结果打印输出
        }
    }
}

internal class Lrc(val timestamp: String, val content: String) {
}