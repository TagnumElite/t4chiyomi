package eu.kanade.t4chiyomi.ui.extension

import android.app.Dialog
import android.os.Bundle
import com.afollestad.materialdialogs.MaterialDialog
import com.bluelinelabs.conductor.Controller
import eu.kanade.t4chiyomi.R
import eu.kanade.t4chiyomi.ui.base.controller.DialogController

class ExtensionTrustDialog<T>(bundle: Bundle? = null) : DialogController(bundle)
        where T : Controller, T: ExtensionTrustDialog.Listener {

    constructor(target: T, signatureHash: String, pkgName: String) : this(Bundle().apply {
        putString(SIGNATURE_KEY, signatureHash)
        putString(PKGNAME_KEY, pkgName)
    }) {
        targetController = target
    }

    override fun onCreateDialog(savedViewState: Bundle?): Dialog {
        return MaterialDialog.Builder(activity!!)
                .title(R.string.untrusted_extension)
                .content(R.string.untrusted_extension_message)
                .positiveText(R.string.ext_trust)
                .negativeText(R.string.ext_uninstall)
                .onPositive { _, _ ->
                    (targetController as? Listener)?.trustSignature(args.getString(SIGNATURE_KEY)!!)
                }
                .onNegative { _, _ ->
                    (targetController as? Listener)?.uninstallExtension(args.getString(PKGNAME_KEY)!!)
                }
                .build()
    }

    private companion object {
        const val SIGNATURE_KEY = "signature_key"
        const val PKGNAME_KEY = "pkgname_key"
    }

    interface Listener {
        fun trustSignature(signatureHash: String)
        fun uninstallExtension(pkgName: String)
    }
}
