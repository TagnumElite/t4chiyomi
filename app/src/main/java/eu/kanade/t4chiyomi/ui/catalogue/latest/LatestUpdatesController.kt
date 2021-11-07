package eu.kanade.t4chiyomi.ui.catalogue.latest

import android.os.Bundle
import android.support.v4.widget.DrawerLayout
import android.view.Menu
import android.view.ViewGroup
import eu.kanade.t4chiyomi.R
import eu.kanade.t4chiyomi.source.CatalogueSource
import eu.kanade.t4chiyomi.ui.catalogue.browse.BrowseCatalogueController
import eu.kanade.t4chiyomi.ui.catalogue.browse.BrowseCataloguePresenter

/**
 * Controller that shows the latest manga from the catalogue. Inherit [BrowseCatalogueController].
 */
class LatestUpdatesController(bundle: Bundle) : BrowseCatalogueController(bundle) {

    constructor(source: CatalogueSource) : this(Bundle().apply {
        putLong(SOURCE_ID_KEY, source.id)
    })

    override fun createPresenter(): BrowseCataloguePresenter {
        return LatestUpdatesPresenter(args.getLong(SOURCE_ID_KEY))
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        super.onPrepareOptionsMenu(menu)
        menu.findItem(R.id.action_search).isVisible = false
        menu.findItem(R.id.action_set_filter).isVisible = false
    }

    override fun createSecondaryDrawer(drawer: DrawerLayout): ViewGroup? {
        return null
    }

    override fun cleanupSecondaryDrawer(drawer: DrawerLayout) {

    }

}
