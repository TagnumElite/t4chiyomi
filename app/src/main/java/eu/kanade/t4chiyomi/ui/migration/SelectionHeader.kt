package eu.kanade.t4chiyomi.ui.migration

import android.view.View
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractHeaderItem
import eu.kanade.t4chiyomi.R
import eu.kanade.t4chiyomi.ui.base.holder.BaseFlexibleViewHolder
import kotlinx.android.synthetic.main.catalogue_main_controller_card.title

/**
 * Item that contains the selection header.
 */
class SelectionHeader : AbstractHeaderItem<SelectionHeader.Holder>() {

    /**
     * Returns the layout resource of this item.
     */
    override fun getLayoutRes(): Int {
        return R.layout.catalogue_main_controller_card
    }

    /**
     * Creates a new view holder for this item.
     */
    override fun createViewHolder(view: View, adapter: FlexibleAdapter<*>): Holder {
        return SelectionHeader.Holder(view, adapter)
    }

    /**
     * Binds this item to the given view holder.
     */
    override fun bindViewHolder(adapter: FlexibleAdapter<*>, holder: Holder,
                                position: Int, payloads: List<Any?>?) {
        // Intentionally empty
    }

    class Holder(view: View, adapter: FlexibleAdapter<*>) : BaseFlexibleViewHolder(view, adapter) {
        init {
            title.text = view.context.getString(R.string.migration_selection_prompt)
        }
    }

    override fun equals(other: Any?): Boolean {
        return other is SelectionHeader
    }

    override fun hashCode(): Int {
        return 0
    }
}
