package eu.kanade.t4chiyomi.ui.extension

import android.annotation.SuppressLint
import android.view.View
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.kanade.t4chiyomi.ui.base.holder.BaseFlexibleViewHolder
import kotlinx.android.synthetic.main.extension_card_header.title

class ExtensionGroupHolder(view: View, adapter: FlexibleAdapter<*>) :
        BaseFlexibleViewHolder(view, adapter) {

    @SuppressLint("SetTextI18n")
    fun bind(item: ExtensionGroupItem) {
        title.text = item.name
    }
}
