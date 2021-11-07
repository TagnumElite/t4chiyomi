package eu.kanade.t4chiyomi.ui.catalogue.filter

import android.view.View
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.viewholders.FlexibleViewHolder
import eu.kanade.t4chiyomi.R
import eu.kanade.t4chiyomi.source.model.Filter
import eu.kanade.t4chiyomi.widget.IgnoreFirstSpinnerListener

open class SelectItem(val filter: Filter.Select<*>) : AbstractFlexibleItem<SelectItem.Holder>() {

    override fun getLayoutRes(): Int {
        return R.layout.navigation_view_spinner
    }

    override fun createViewHolder(view: View, adapter: FlexibleAdapter<*>): Holder {
        return Holder(view, adapter)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<*>, holder: Holder, position: Int, payloads: List<Any?>?) {
        holder.text.text = filter.name + ": "

        val spinner = holder.spinner
        spinner.prompt = filter.name
        spinner.adapter = ArrayAdapter<Any>(holder.itemView.context,
                android.R.layout.simple_spinner_item, filter.values).apply {
            setDropDownViewResource(R.layout.common_spinner_item)
        }
        spinner.onItemSelectedListener = IgnoreFirstSpinnerListener { pos ->
            filter.state = pos
        }
        spinner.setSelection(filter.state)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return filter == (other as SelectItem).filter
    }

    override fun hashCode(): Int {
        return filter.hashCode()
    }

    class Holder(view: View, adapter: FlexibleAdapter<*>) : FlexibleViewHolder(view, adapter) {

        val text: TextView = itemView.findViewById(R.id.nav_view_item_text)
        val spinner: Spinner = itemView.findViewById(R.id.nav_view_item)
    }
}