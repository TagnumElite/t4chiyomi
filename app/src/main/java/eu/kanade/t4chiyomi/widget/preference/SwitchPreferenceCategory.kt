package eu.kanade.t4chiyomi.widget.preference

import android.annotation.TargetApi
import android.content.Context
import android.content.res.TypedArray
import android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH
import android.support.v7.preference.PreferenceCategory
import android.support.v7.preference.PreferenceViewHolder
import android.support.v7.widget.SwitchCompat
import android.util.AttributeSet
import android.view.View
import android.widget.Checkable
import android.widget.CompoundButton
import android.widget.TextView
import eu.kanade.t4chiyomi.R
import eu.kanade.t4chiyomi.util.getResourceColor

class SwitchPreferenceCategory @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null)
: PreferenceCategory(
        context,
        attrs,
        R.attr.switchPreferenceCompatStyle),
CompoundButton.OnCheckedChangeListener {

    private var mChecked = false

    private var mCheckedSet = false

    override fun onBindViewHolder(holder: PreferenceViewHolder) {
        super.onBindViewHolder(holder)
        val titleView = holder.findViewById(android.R.id.title) as TextView
        titleView.setTextColor(context.getResourceColor(R.attr.colorAccent))
        syncSwitchView(holder)
    }

    private fun syncSwitchView(holder: PreferenceViewHolder) {
        val switchView = holder.findViewById(R.id.switchWidget)
        syncSwitchView(switchView)
    }

    @TargetApi(ICE_CREAM_SANDWICH)
    private fun syncSwitchView(view: View) {
        if (view is Checkable) {
            val isChecked = view.isChecked
            if (isChecked == mChecked) return

            if (view is SwitchCompat) {
                view.setOnCheckedChangeListener(null)
            }

            view.toggle()

            if (view is SwitchCompat) {
                view.setOnCheckedChangeListener(this)
            }
        }
    }

    override fun onCheckedChanged(buttonView: CompoundButton, isChecked: Boolean) {
        if (!callChangeListener(isChecked)) {
            buttonView.isChecked = !isChecked
        } else {
            setChecked(isChecked)
        }
    }

    override fun onClick() {
        super.onClick()

        val newValue = !isChecked()
        if (callChangeListener(newValue)) {
            setChecked(newValue)
        }
    }

    /**
     * Sets the checked state and saves it to the [SharedPreferences].
     *
     * @param checked The checked state.
     */
    fun setChecked(checked: Boolean) {
        // Always persist/notify the first time; don't assume the field's default of false.
        val changed = mChecked != checked
        if (changed || !mCheckedSet) {
            mChecked = checked
            mCheckedSet = true
            persistBoolean(checked)
            if (changed) {
                notifyDependencyChange(shouldDisableDependents())
                notifyChanged()
            }
        }
    }

    /**
     * Returns the checked state.
     *
     * @return The checked state.
     */
    fun isChecked(): Boolean {
        return mChecked
    }

    override fun isEnabled(): Boolean {
        return true
    }

    override fun shouldDisableDependents(): Boolean {
        return false
    }

    override fun onGetDefaultValue(a: TypedArray, index: Int): Any {
        return a.getBoolean(index, false)
    }

    override fun onSetInitialValue(restoreValue: Boolean, defaultValue: Any?) {
        setChecked(if (restoreValue)
            getPersistedBoolean(mChecked)
        else
            defaultValue as Boolean)
    }

}