package eu.kanade.t4chiyomi.widget

import android.support.v7.widget.RecyclerView
import android.view.ViewGroup
import com.nononsenseapps.filepicker.AbstractFilePickerFragment
import com.nononsenseapps.filepicker.FilePickerActivity
import com.nononsenseapps.filepicker.FilePickerFragment
import com.nononsenseapps.filepicker.LogicHandler
import eu.kanade.t4chiyomi.R
import eu.kanade.t4chiyomi.util.inflate
import java.io.File

class CustomLayoutPickerActivity : FilePickerActivity() {

    override fun getFragment(startPath: String?, mode: Int, allowMultiple: Boolean, allowCreateDir: Boolean):
            AbstractFilePickerFragment<File> {
        val fragment = CustomLayoutFilePickerFragment()
        fragment.setArgs(startPath, mode, allowMultiple, allowCreateDir)
        return fragment
    }
}

class CustomLayoutFilePickerFragment : FilePickerFragment() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        when (viewType) {
            LogicHandler.VIEWTYPE_DIR -> {
                val view = parent.inflate(R.layout.common_listitem_dir)
                return DirViewHolder(view)
            }
            else -> return super.onCreateViewHolder(parent, viewType)
        }
    }
}