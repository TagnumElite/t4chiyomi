package eu.kanade.t4chiyomi.ui.base.controller

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.bluelinelabs.conductor.Controller
import com.bluelinelabs.conductor.ControllerChangeHandler
import com.bluelinelabs.conductor.ControllerChangeType
import com.bluelinelabs.conductor.RestoreViewOnCreateController
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.*
import timber.log.Timber

abstract class BaseController(bundle: Bundle? = null) : RestoreViewOnCreateController(bundle),
        LayoutContainer {

    init {
        addLifecycleListener(object : LifecycleListener() {
            override fun postCreateView(controller: Controller, view: View) {
                onViewCreated(view)
            }

            override fun preCreateView(controller: Controller) {
                Timber.d("Create view for ${controller.instance()}")
            }

            override fun preAttach(controller: Controller, view: View) {
                Timber.d("Attach view for ${controller.instance()}")
            }

            override fun preDetach(controller: Controller, view: View) {
                Timber.d("Detach view for ${controller.instance()}")
            }

            override fun preDestroyView(controller: Controller, view: View) {
                Timber.d("Destroy view for ${controller.instance()}")
            }
        })
    }

    override val containerView: View?
        get() = view

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup, savedViewState: Bundle?): View {
        return inflateView(inflater, container)
    }

    override fun onDestroyView(view: View) {
        super.onDestroyView(view)
        clearFindViewByIdCache()
    }

    abstract fun inflateView(inflater: LayoutInflater, container: ViewGroup): View

    open fun onViewCreated(view: View) { }

    override fun onChangeStarted(handler: ControllerChangeHandler, type: ControllerChangeType) {
        if (type.isEnter) {
            setTitle()
        }
        super.onChangeStarted(handler, type)
    }

    open fun getTitle(): String? {
        return null
    }

    fun setTitle() {
        var parentController = parentController
        while (parentController != null) {
            if (parentController is BaseController && parentController.getTitle() != null) {
                return
            }
            parentController = parentController.parentController
        }

        (activity as? AppCompatActivity)?.supportActionBar?.title = getTitle()
    }

    private fun Controller.instance(): String {
        return "${javaClass.simpleName}@${Integer.toHexString(hashCode())}"
    }

    /**
     * Workaround for disappearing menu items when collapsing an expandable item like a SearchView.
     * This method should be removed when fixed upstream.
     * Issue link: https://issuetracker.google.com/issues/37657375
     */
    fun MenuItem.fixExpand() {
        setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem): Boolean {
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
                activity?.invalidateOptionsMenu()
                return true
            }
        })
    }

}
