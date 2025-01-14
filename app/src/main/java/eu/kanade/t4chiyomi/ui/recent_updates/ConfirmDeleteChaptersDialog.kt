package eu.kanade.t4chiyomi.ui.recent_updates

import android.app.Dialog
import android.os.Bundle
import com.afollestad.materialdialogs.MaterialDialog
import com.bluelinelabs.conductor.Controller
import eu.kanade.t4chiyomi.R
import eu.kanade.t4chiyomi.ui.base.controller.DialogController

class ConfirmDeleteChaptersDialog<T>(bundle: Bundle? = null) : DialogController(bundle)
        where T : Controller, T : ConfirmDeleteChaptersDialog.Listener {

    private var chaptersToDelete = emptyList<RecentChapterItem>()

    constructor(target: T, chaptersToDelete: List<RecentChapterItem>) : this() {
        this.chaptersToDelete = chaptersToDelete
        targetController = target
    }

    override fun onCreateDialog(savedViewState: Bundle?): Dialog {
        return MaterialDialog.Builder(activity!!)
                .content(R.string.confirm_delete_chapters)
                .positiveText(android.R.string.yes)
                .negativeText(android.R.string.no)
                .onPositive { _, _ ->
                    (targetController as? Listener)?.deleteChapters(chaptersToDelete)
                }
                .build()
    }

    interface Listener {
        fun deleteChapters(chaptersToDelete: List<RecentChapterItem>)
    }
}