package eu.kanade.t4chiyomi.ui.extension

import android.app.Application
import android.os.Bundle
import eu.kanade.t4chiyomi.R
import eu.kanade.t4chiyomi.extension.ExtensionManager
import eu.kanade.t4chiyomi.extension.model.Extension
import eu.kanade.t4chiyomi.extension.model.InstallStep
import eu.kanade.t4chiyomi.ui.base.presenter.BasePresenter
import eu.kanade.t4chiyomi.util.LocaleHelper
import rx.Observable
import rx.Subscription
import rx.android.schedulers.AndroidSchedulers
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get
import java.util.concurrent.TimeUnit

private typealias ExtensionTuple
        = Triple<List<Extension.Installed>, List<Extension.Untrusted>, List<Extension.Available>>

/**
 * Presenter of [ExtensionController].
 */
open class ExtensionPresenter(
        private val extensionManager: ExtensionManager = Injekt.get()
) : BasePresenter<ExtensionController>() {

    private var extensions = emptyList<ExtensionItem>()

    private var currentDownloads = hashMapOf<String, InstallStep>()

    override fun onCreate(savedState: Bundle?) {
        super.onCreate(savedState)

        extensionManager.findAvailableExtensions()
        bindToExtensionsObservable()
    }

    private fun bindToExtensionsObservable(): Subscription {
        val installedObservable = extensionManager.getInstalledExtensionsObservable()
        val untrustedObservable = extensionManager.getUntrustedExtensionsObservable()
        val availableObservable = extensionManager.getAvailableExtensionsObservable()
                .startWith(emptyList<Extension.Available>())

        return Observable.combineLatest(installedObservable, untrustedObservable, availableObservable)
                { installed, untrusted, available -> Triple(installed, untrusted, available) }
                .debounce(100, TimeUnit.MILLISECONDS)
                .map(::toItems)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeLatestCache({ view, _ -> view.setExtensions(extensions) })
    }

    @Synchronized
    private fun toItems(tuple: ExtensionTuple): List<ExtensionItem> {
        val context = Injekt.get<Application>()

        val (installed, untrusted, available) = tuple

        val items = mutableListOf<ExtensionItem>()

        val installedSorted = installed.sortedWith(compareBy({ !it.hasUpdate }, { it.pkgName }))
        val untrustedSorted = untrusted.sortedBy { it.pkgName }
        val availableSorted = available
                // Filter out already installed extensions
                .filter { avail -> installed.none { it.pkgName == avail.pkgName }
                        && untrusted.none { it.pkgName == avail.pkgName } }
                .sortedBy { it.pkgName }

        if (installedSorted.isNotEmpty() || untrustedSorted.isNotEmpty()) {
            val header = ExtensionGroupItem(context.getString(R.string.ext_installed), installedSorted.size + untrustedSorted.size)
            items += installedSorted.map { extension ->
                ExtensionItem(extension, header, currentDownloads[extension.pkgName])
            }
            items += untrustedSorted.map { extension ->
                ExtensionItem(extension, header)
            }
        }
        if (availableSorted.isNotEmpty()) {
            val availableGroupedByLang = availableSorted
                    .groupBy { LocaleHelper.getDisplayName(it.lang, context) }
                    .toSortedMap()

            availableGroupedByLang
                    .forEach {
                        val header = ExtensionGroupItem(it.key, it.value.size)
                        items += it.value.map { extension ->
                            ExtensionItem(extension, header, currentDownloads[extension.pkgName])
                        }
                    }
        }

        this.extensions = items
        return items
    }

    @Synchronized
    private fun updateInstallStep(extension: Extension, state: InstallStep): ExtensionItem? {
        val extensions = extensions.toMutableList()
        val position = extensions.indexOfFirst { it.extension.pkgName == extension.pkgName }

        return if (position != -1) {
            val item = extensions[position].copy(installStep = state)
            extensions[position] = item

            this.extensions = extensions
            item
        } else {
            null
        }
    }

    fun installExtension(extension: Extension.Available) {
        extensionManager.installExtension(extension).subscribeToInstallUpdate(extension)
    }

    fun updateExtension(extension: Extension.Installed) {
        extensionManager.updateExtension(extension).subscribeToInstallUpdate(extension)
    }

    private fun Observable<InstallStep>.subscribeToInstallUpdate(extension: Extension) {
        this.doOnNext { currentDownloads[extension.pkgName] = it }
                .doOnUnsubscribe { currentDownloads.remove(extension.pkgName) }
                .map { state -> updateInstallStep(extension, state) }
                .subscribeWithView({ view, item ->
                    if (item != null) {
                        view.downloadUpdate(item)
                    }
                })
    }

    fun uninstallExtension(pkgName: String) {
        extensionManager.uninstallExtension(pkgName)
    }

    fun findAvailableExtensions() {
        extensionManager.findAvailableExtensions()
    }

    fun trustSignature(signatureHash: String) {
        extensionManager.trustSignature(signatureHash)
    }

}
