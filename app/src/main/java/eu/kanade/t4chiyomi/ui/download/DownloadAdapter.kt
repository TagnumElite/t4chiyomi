package eu.kanade.t4chiyomi.ui.download

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import eu.kanade.t4chiyomi.R
import eu.kanade.t4chiyomi.data.download.model.Download
import eu.kanade.t4chiyomi.util.inflate

/**
 * Adapter storing a list of downloads.
 *
 * @param context the context of the fragment containing this adapter.
 */
class DownloadAdapter : RecyclerView.Adapter<DownloadHolder>() {

    private var items = emptyList<Download>()

    init {
        setHasStableIds(true)
    }

    /**
     * Sets a list of downloads in the adapter.
     *
     * @param downloads the list to set.
     */
    fun setItems(downloads: List<Download>) {
        items = downloads
        notifyDataSetChanged()
    }

    /**
     * Returns the number of downloads in the adapter
     */
    override fun getItemCount(): Int {
        return items.size
    }

    /**
     * Returns the identifier for a download.
     *
     * @param position the position in the adapter.
     * @return an identifier for the item.
     */
    override fun getItemId(position: Int): Long {
        return items[position].chapter.id!!
    }

    /**
     * Creates a new view holder.
     *
     * @param parent the parent view.
     * @param viewType the type of the holder.
     * @return a new view holder for a manga.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadHolder {
        val view = parent.inflate(R.layout.download_item)
        return DownloadHolder(view)
    }

    /**
     * Binds a holder with a new position.
     *
     * @param holder the holder to bind.
     * @param position the position to bind.
     */
    override fun onBindViewHolder(holder: DownloadHolder, position: Int) {
        val download = items[position]
        holder.onSetValues(download)
    }

}
