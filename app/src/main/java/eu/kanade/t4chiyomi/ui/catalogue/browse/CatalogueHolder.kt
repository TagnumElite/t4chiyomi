package eu.kanade.t4chiyomi.ui.catalogue.browse

import android.view.View
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.kanade.t4chiyomi.data.database.models.Manga
import eu.kanade.t4chiyomi.ui.base.holder.BaseFlexibleViewHolder

/**
 * Generic class used to hold the displayed data of a manga in the catalogue.
 *
 * @param view the inflated view for this holder.
 * @param adapter the adapter handling this holder.
 */
abstract class CatalogueHolder(view: View, adapter: FlexibleAdapter<*>) :
        BaseFlexibleViewHolder(view, adapter) {

    /**
     * Method called from [CatalogueAdapter.onBindViewHolder]. It updates the data for this
     * holder with the given manga.
     *
     * @param manga the manga to bind.
     */
    abstract fun onSetValues(manga: Manga)


    /**
     * Updates the image for this holder. Useful to update the image when the manga is initialized
     * and the url is now known.
     *
     * @param manga the manga to bind.
     */
    abstract fun setImage(manga: Manga)
}
