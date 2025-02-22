package eu.kanade.t4chiyomi.ui.recent_updates

import android.view.View
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractSectionableItem
import eu.kanade.t4chiyomi.R
import eu.kanade.t4chiyomi.data.database.models.Chapter
import eu.kanade.t4chiyomi.data.database.models.Manga
import eu.kanade.t4chiyomi.data.download.model.Download

class RecentChapterItem(val chapter: Chapter, val manga: Manga, header: DateItem) :
        AbstractSectionableItem<RecentChapterHolder, DateItem>(header) {

    private var _status: Int = 0

    var status: Int
        get() = download?.status ?: _status
        set(value) { _status = value }

    @Transient var download: Download? = null

    val isDownloaded: Boolean
        get() = status == Download.DOWNLOADED

    override fun getLayoutRes(): Int {
        return R.layout.recent_chapters_item
    }

    override fun createViewHolder(view: View, adapter: FlexibleAdapter<*>): RecentChapterHolder {
        return RecentChapterHolder(view , adapter as RecentChaptersAdapter)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<*>,
                                holder: RecentChapterHolder,
                                position: Int,
                                payloads: List<Any?>?) {

        holder.bind(this)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other is RecentChapterItem) {
            return chapter.id!! == other.chapter.id!!
        }
        return false
    }

    override fun hashCode(): Int {
        return chapter.id!!.hashCode()
    }

}