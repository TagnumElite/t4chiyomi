package eu.kanade.t4chiyomi.widget.preference

import android.content.Context
import android.support.v7.preference.Preference
import android.support.v7.preference.PreferenceViewHolder
import android.util.AttributeSet
import eu.kanade.t4chiyomi.R
import kotlinx.android.synthetic.main.pref_widget_imageview.view.*

class LoginPreference @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
        Preference(context, attrs) {

    init {
        widgetLayoutResource = R.layout.pref_widget_imageview
    }

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)

        holder.itemView.image_view.setImageResource(if (getPersistedString("").isNullOrEmpty())
            android.R.color.transparent
        else
            R.drawable.ic_done_green_24dp)
    }

    override public fun notifyChanged() {
        super.notifyChanged()
    }

}
