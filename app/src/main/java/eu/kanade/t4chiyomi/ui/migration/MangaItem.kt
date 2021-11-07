package eu.kanade.t4chiyomi.ui.migration

import android.view.View
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.kanade.t4chiyomi.R
import eu.kanade.t4chiyomi.data.database.models.Manga

class MangaItem(val manga: Manga) : AbstractFlexibleItem<MangaHolder>() {

    override fun getLayoutRes(): Int {
        return R.layout.catalogue_list_item
    }

    override fun createViewHolder(view: View, adapter: FlexibleAdapter<*>): MangaHolder {
        return MangaHolder(view, adapter)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<*>,
                                holder: MangaHolder,
                                position: Int,
                                payloads: List<Any?>?) {

        holder.bind(this)
    }

    override fun equals(other: Any?): Boolean {
        if (other is MangaItem) {
            return manga.id == other.manga.id
        }
        return false
    }

    override fun hashCode(): Int {
        return manga.id!!.hashCode()
    }
}