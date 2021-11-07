package eu.kanade.t4chiyomi.ui.catalogue

import android.view.View
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractSectionableItem
import eu.kanade.t4chiyomi.R
import eu.kanade.t4chiyomi.source.CatalogueSource

/**
 * Item that contains source information.
 *
 * @param source Instance of [CatalogueSource] containing source information.
 * @param header The header for this item.
 */
data class SourceItem(val source: CatalogueSource, val header: LangItem? = null) :
        AbstractSectionableItem<SourceHolder, LangItem>(header) {

    /**
     * Returns the layout resource of this item.
     */
    override fun getLayoutRes(): Int {
        return R.layout.catalogue_main_controller_card_item
    }

    /**
     * Creates a new view holder for this item.
     */
    override fun createViewHolder(view: View, adapter: FlexibleAdapter<*>): SourceHolder {
        return SourceHolder(view, adapter as CatalogueAdapter)
    }

    /**
     * Binds this item to the given view holder.
     */
    override fun bindViewHolder(adapter: FlexibleAdapter<*>, holder: SourceHolder,
                                position: Int, payloads: List<Any?>?) {

        holder.bind(this)
    }

}