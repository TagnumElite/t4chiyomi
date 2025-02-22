package eu.kanade.t4chiyomi.widget

import android.content.Context
import android.text.SpannableStringBuilder
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import eu.kanade.t4chiyomi.R
import eu.kanade.t4chiyomi.util.inflate
import kotlinx.android.synthetic.main.download_custom_amount.view.*
import timber.log.Timber

/**
 * Custom dialog to select how many chapters to download.
 */
class DialogCustomDownloadView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
        LinearLayout(context, attrs) {

    /**
     * Current amount of custom download chooser.
     */
    var amount: Int = 0
        private set

    /**
     * Minimal value of custom download chooser.
     */
    private var min = 0

    /**
     * Maximal value of custom download chooser.
     */
    private var max = 0

    init {
        // Add view to stack
        addView(inflate(R.layout.download_custom_amount))
    }


    /**
     * Called when view is added
     *
     * @param child
     */
    override fun onViewAdded(child: View) {
        super.onViewAdded(child)

        // Set download count to 0.
        myNumber.text = SpannableStringBuilder(getAmount(0).toString())

        // When user presses button decrease amount by 10.
        btn_decrease_10.setOnClickListener {
            myNumber.text = SpannableStringBuilder(getAmount(amount - 10).toString())
        }

        // When user presses button increase amount by 10.
        btn_increase_10.setOnClickListener {
            myNumber.text = SpannableStringBuilder(getAmount(amount + 10).toString())
        }

        // When user presses button decrease amount by 1.
        btn_decrease.setOnClickListener {
            myNumber.text = SpannableStringBuilder(getAmount(amount - 1).toString())
        }

        // When user presses button increase amount by 1.
        btn_increase.setOnClickListener {
            myNumber.text = SpannableStringBuilder(getAmount(amount + 1).toString())
        }

        // When user inputs custom number set amount equal to input.
        myNumber.addTextChangedListener(object : SimpleTextWatcher() {
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                try {
                    amount = getAmount(Integer.parseInt(s.toString()))
                } catch (error: NumberFormatException) {
                    // Catch NumberFormatException to prevent parse exception when input is empty.
                    Timber.e(error)
                }
            }
        })
    }

    /**
     * Set min max of custom download amount chooser.
     * @param min minimal downloads
     * @param max maximal downloads
     */
    fun setMinMax(min: Int, max: Int) {
        this.min = min
        this.max = max
    }

    /**
     * Returns amount to download.
     * if minimal downloads is less than input return minimal downloads.
     * if Maximal downloads is more than input return maximal downloads.
     *
     * @return amount to download.
     */
    private fun getAmount(input: Int): Int {
        return when {
            input > max -> max
            input < min -> min
            else -> input
        }
    }
}
