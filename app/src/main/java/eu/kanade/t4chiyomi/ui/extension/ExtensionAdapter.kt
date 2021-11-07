package eu.kanade.t4chiyomi.ui.extension

import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import eu.kanade.t4chiyomi.R
import eu.kanade.t4chiyomi.util.getResourceColor

/**
 * Adapter that holds the catalogue cards.
 *
 * @param controller instance of [ExtensionController].
 */
class ExtensionAdapter(val controller: ExtensionController) :
        FlexibleAdapter<IFlexible<*>>(null, controller, true) {

    val cardBackground = controller.activity!!.getResourceColor(R.attr.background_card)

    init {
        setDisplayHeadersAtStartUp(true)
    }

    /**
     * Listener for browse item clicks.
     */
    val buttonClickListener: ExtensionAdapter.OnButtonClickListener = controller

    interface OnButtonClickListener {
        fun onButtonClick(position: Int)
    }
}
