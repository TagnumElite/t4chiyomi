package eu.kanade.t4chiyomi.ui.reader.model

import eu.kanade.t4chiyomi.source.model.Page
import java.io.InputStream

class ReaderPage(
        index: Int,
        url: String = "",
        imageUrl: String? = null,
        var stream: (() -> InputStream)? = null
) : Page(index, url, imageUrl, null) {

    lateinit var chapter: ReaderChapter

}
