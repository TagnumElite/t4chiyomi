package eu.kanade.t4chiyomi.ui.main

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import com.afollestad.materialdialogs.MaterialDialog
import eu.kanade.t4chiyomi.BuildConfig
import eu.kanade.t4chiyomi.R
import eu.kanade.t4chiyomi.ui.base.controller.DialogController
import it.gmariotti.changelibs.library.view.ChangeLogRecyclerView

class ChangelogDialogController : DialogController() {

    override fun onCreateDialog(savedState: Bundle?): Dialog {
        val activity = activity!!
        val view = WhatsNewRecyclerView(activity)
        return MaterialDialog.Builder(activity)
                .title(if (BuildConfig.DEBUG) "Notices" else "Changelog")
                .customView(view, false)
                .positiveText(android.R.string.yes)
                .build()
    }

    class WhatsNewRecyclerView(context: Context) : ChangeLogRecyclerView(context) {
        override fun initAttrs(attrs: AttributeSet?, defStyle: Int) {
            mRowLayoutId = R.layout.changelog_row_layout
            mRowHeaderLayoutId = R.layout.changelog_header_layout
            mChangeLogFileResourceId = if (BuildConfig.DEBUG) R.raw.changelog_debug else R.raw.changelog_release
        }
    }
}