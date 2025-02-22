package eu.kanade.t4chiyomi.ui.catalogue.filter

import android.support.design.widget.TextInputLayout
import android.view.View
import android.widget.EditText
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.viewholders.FlexibleViewHolder
import eu.kanade.t4chiyomi.R
import eu.kanade.t4chiyomi.source.model.Filter
import eu.kanade.t4chiyomi.widget.SimpleTextWatcher

open class TextItem(val filter: Filter.Text) : AbstractFlexibleItem<TextItem.Holder>() {

    override fun getLayoutRes(): Int {
        return R.layout.navigation_view_text
    }

    override fun createViewHolder(view: View, adapter: FlexibleAdapter<*>): Holder {
        return Holder(view, adapter)
    }

    override fun bindViewHolder(adapter: FlexibleAdapter<*>, holder: Holder, position: Int, payloads: List<Any?>?) {
        holder.wrapper.hint = filter.name
        holder.edit.setText(filter.state)
        holder.edit.addTextChangedListener(object : SimpleTextWatcher() {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                filter.state = s.toString()
            }
        })
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        return filter == (other as TextItem).filter
    }

    override fun hashCode(): Int {
        return filter.hashCode()
    }

    class Holder(view: View, adapter: FlexibleAdapter<*>) : FlexibleViewHolder(view, adapter) {

        val wrapper: TextInputLayout = itemView.findViewById(R.id.nav_view_item_wrapper)
        val edit: EditText = itemView.findViewById(R.id.nav_view_item)
    }
}