package eu.kanade.t4chiyomi.ui.manga.chapter

import android.app.Dialog
import android.os.Bundle
import com.afollestad.materialdialogs.MaterialDialog
import com.bluelinelabs.conductor.Controller
import eu.kanade.t4chiyomi.R
import eu.kanade.t4chiyomi.ui.base.controller.DialogController
import eu.kanade.t4chiyomi.widget.DialogCustomDownloadView

/**
 * Dialog used to let user select amount of chapters to download.
 */
class DownloadCustomChaptersDialog<T> : DialogController
        where T : Controller, T : DownloadCustomChaptersDialog.Listener {

    /**
     * Maximum number of chapters to download in download chooser.
     */
    private val maxChapters: Int

    /**
     * Initialize dialog.
     * @param maxChapters maximal number of chapters that user can download.
     */
    constructor(target: T, maxChapters: Int) : super(Bundle().apply {
        // Add maximum number of chapters to download value to bundle.
        putInt(KEY_ITEM_MAX, maxChapters)
    }) {
        targetController = target
        this.maxChapters = maxChapters
    }

    /**
     * Restore dialog.
     * @param bundle bundle containing data from state restore.
     */
    @Suppress("unused")
    constructor(bundle: Bundle) : super(bundle) {
        // Get maximum chapters to download from bundle
        val maxChapters = bundle.getInt(KEY_ITEM_MAX, 0)
        this.maxChapters = maxChapters
    }

    /**
     * Called when dialog is being created.
     */
    override fun onCreateDialog(savedViewState: Bundle?): Dialog {
        val activity = activity!!

        // Initialize view that lets user select number of chapters to download.
        val view = DialogCustomDownloadView(activity).apply {
            setMinMax(0, maxChapters)
        }

        // Build dialog.
        // when positive dialog is pressed call custom listener.
        return MaterialDialog.Builder(activity)
                .title(R.string.custom_download)
                .customView(view, true)
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .onPositive { _, _ ->
                    (targetController as? Listener)?.downloadCustomChapters(view.amount)
                }
                .build()
    }

    interface Listener {
        fun downloadCustomChapters(amount: Int)
    }

    private companion object {
        // Key to retrieve max chapters from bundle on process death.
        const val KEY_ITEM_MAX = "DownloadCustomChaptersDialog.int.maxChapters"
    }
}