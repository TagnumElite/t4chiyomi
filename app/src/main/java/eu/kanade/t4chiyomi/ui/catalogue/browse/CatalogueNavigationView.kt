package eu.kanade.t4chiyomi.ui.catalogue.browse

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import eu.kanade.t4chiyomi.R
import eu.kanade.t4chiyomi.util.inflate
import eu.kanade.t4chiyomi.widget.SimpleNavigationView
import kotlinx.android.synthetic.main.catalogue_drawer_content.view.*


class CatalogueNavigationView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null)
    : SimpleNavigationView(context, attrs) {

    val adapter: FlexibleAdapter<IFlexible<*>> = FlexibleAdapter<IFlexible<*>>(null)
            .setDisplayHeadersAtStartUp(true)
            .setStickyHeaders(true)

    var onSearchClicked = {}

    var onResetClicked = {}

    init {
        recycler.adapter = adapter
        recycler.setHasFixedSize(true)
        val view = inflate(R.layout.catalogue_drawer_content)
        ((view as ViewGroup).getChildAt(1) as ViewGroup).addView(recycler)
        addView(view)
        title.text = context.getString(R.string.source_search_options)
        search_btn.setOnClickListener { onSearchClicked() }
        reset_btn.setOnClickListener { onResetClicked() }
    }

    fun setFilters(items: List<IFlexible<*>>) {
        adapter.updateDataSet(items)
    }

}
