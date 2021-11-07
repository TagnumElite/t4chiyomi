package eu.kanade.t4chiyomi.widget.preference

import android.os.Bundle
import android.view.View
import eu.kanade.t4chiyomi.R
import eu.kanade.t4chiyomi.source.Source
import eu.kanade.t4chiyomi.source.SourceManager
import eu.kanade.t4chiyomi.source.online.LoginSource
import eu.kanade.t4chiyomi.util.toast
import kotlinx.android.synthetic.main.pref_account_login.view.*
import rx.android.schedulers.AndroidSchedulers
import rx.schedulers.Schedulers
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

class SourceLoginDialog(bundle: Bundle? = null) : LoginDialogPreference(bundle) {

    private val source = Injekt.get<SourceManager>().get(args.getLong("key")) as LoginSource

    constructor(source: Source) : this(Bundle().apply { putLong("key", source.id) })

    override fun setCredentialsOnView(view: View) = with(view) {
        dialog_title.text = context.getString(R.string.login_title, source.toString())
        username.setText(preferences.sourceUsername(source))
        password.setText(preferences.sourcePassword(source))
    }

    override fun checkLogin() {
        requestSubscription?.unsubscribe()

        v?.apply {
            if (username.text.isEmpty() || password.text.isEmpty())
                return

            login.progress = 1

            requestSubscription = source.login(username.text.toString(), password.text.toString())
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ logged ->
                        if (logged) {
                            preferences.setSourceCredentials(source,
                                    username.text.toString(),
                                    password.text.toString())

                            dialog?.dismiss()
                            context.toast(R.string.login_success)
                        } else {
                            preferences.setSourceCredentials(source, "", "")
                            login.progress = -1
                        }
                    }, { error ->
                        login.progress = -1
                        login.setText(R.string.unknown_error)
                        error.message?.let { context.toast(it) }
                    })
        }
    }

    override fun onDialogClosed() {
        super.onDialogClosed()
        (targetController as? Listener)?.loginDialogClosed(source)
    }

    interface Listener {
        fun loginDialogClosed(source: LoginSource)
    }

}
