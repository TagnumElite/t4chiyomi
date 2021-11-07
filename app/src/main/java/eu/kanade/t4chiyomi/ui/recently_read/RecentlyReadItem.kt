package eu.kanade.t4chiyomi.ui.recently_read

import android.view.View
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.kanade.t4chiyomi.R
import eu.kanade.t4chiyomi.data.database.models.MangaChapterHistory

class RecentlyReadItem(val mch: MangaChapterHistory) : AbstractFlexibleItem<RecentlyReadHolder>() {

    override fun getLayoutRes(): Int {
        return R.layout.recently_read_item
    }

    override fun createViewHolder(view: View, adapter: FlexibleAdapter<*>): RecentlyReadHolder {
        return RecentlyReadHolder(view, adapter as RecentlyReadAdapter)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<*>,
                                holder: RecentlyReadHolder,
                                position: Int,
                                payloads: List<Any?>?) {

        holder.bind(mch)
    }

    override fun equals(other: Any?): Boolean {
        if (other is RecentlyReadItem) {
            return mch.manga.id == other.mch.manga.id
        }
        return false
    }

    override fun hashCode(): Int {
        return mch.manga.id!!.hashCode()
    }
}