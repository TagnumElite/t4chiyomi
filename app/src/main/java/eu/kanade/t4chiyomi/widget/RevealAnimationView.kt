package eu.kanade.t4chiyomi.widget

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.ViewAnimationUtils

@TargetApi(Build.VERSION_CODES.LOLLIPOP)
class RevealAnimationView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null) :
        View(context, attrs) {

    /**
     * Hides the animation view with a animation
     *
     * @param centerX x starting point
     * @param centerY y starting point
     * @param initialRadius size of radius of animation
     */
    fun hideRevealEffect(centerX: Int, centerY: Int, initialRadius: Int) {
        if (Build.VERSION.SDK_INT >= 21) {

            // Make the view visible.
            this.visibility = View.VISIBLE

            // Create the animation (the final radius is zero).
            val anim = ViewAnimationUtils.createCircularReveal(
                    this, centerX, centerY, initialRadius.toFloat(), 0f)

            // Set duration of animation.
            anim.duration = 500

            // make the view invisible when the animation is done
            anim.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    super.onAnimationEnd(animation)
                    this@RevealAnimationView.visibility = View.INVISIBLE
                }
            })

            anim.start()
        }
    }

    /**
     * Fills the animation view with a animation
     *
     * @param centerX x starting point
     * @param centerY y starting point
     * @param listener animation listener
     *
     * @return sdk version lower then 21
     */
    fun showRevealEffect(centerX: Int, centerY: Int, listener: Animator.AnimatorListener): Boolean {
        if (Build.VERSION.SDK_INT >= 21) {

            this.visibility = View.VISIBLE

            val height = this.height

            // Create animation
            val anim = ViewAnimationUtils.createCircularReveal(
                    this, centerX, centerY, 0f, height.toFloat())

            // Set duration of animation
            anim.duration = 350

            anim.addListener(listener)
            anim.start()
            return true
        }
        return false
    }


}
