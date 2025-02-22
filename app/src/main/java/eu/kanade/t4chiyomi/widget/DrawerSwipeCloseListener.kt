package eu.kanade.t4chiyomi.widget

import android.support.v4.widget.DrawerLayout
import android.view.View
import android.view.ViewGroup

class DrawerSwipeCloseListener(
        private val drawer: DrawerLayout,
        private val navigationView: ViewGroup
) : DrawerLayout.SimpleDrawerListener() {

    override fun onDrawerOpened(drawerView: View) {
        if (drawerView == navigationView) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED, drawerView)
        }
    }

    override fun onDrawerClosed(drawerView: View) {
        if (drawerView == navigationView) {
            drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED, drawerView)
        }
    }
}