package eu.kanade.t4chiyomi.extension.util

import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import dalvik.system.PathClassLoader
import eu.kanade.t4chiyomi.data.preference.PreferencesHelper
import eu.kanade.t4chiyomi.data.preference.getOrDefault
import eu.kanade.t4chiyomi.extension.model.Extension
import eu.kanade.t4chiyomi.extension.model.LoadResult
import eu.kanade.t4chiyomi.source.CatalogueSource
import eu.kanade.t4chiyomi.source.Source
import eu.kanade.t4chiyomi.source.SourceFactory
import eu.kanade.t4chiyomi.util.Hash
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import timber.log.Timber
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

/**
 * Class that handles the loading of the extensions installed in the system.
 */
@SuppressLint("PackageManagerGetSignatures")
internal object ExtensionLoader {

    private const val EXTENSION_FEATURE = "tachiyomi.extension"
    private const val METADATA_SOURCE_CLASS = "tachiyomi.extension.class"
    const val LIB_VERSION_MIN = 1.0
    const val LIB_VERSION_MAX = 1.2

    private const val PACKAGE_FLAGS = PackageManager.GET_CONFIGURATIONS or PackageManager.GET_SIGNATURES

    /**
     * List of the trusted signatures.
     */
    var trustedSignatures = mutableSetOf<String>() +
            Injekt.get<PreferencesHelper>().trustedSignatures().getOrDefault() +
            // inorichi's key
            "7ce04da7773d41b489f4693a366c36bcd0a11fc39b547168553c285bd7348e23"

    /**
     * Return a list of all the installed extensions initialized concurrently.
     *
     * @param context The application context.
     */
    fun loadExtensions(context: Context): List<LoadResult> {
        val pkgManager = context.packageManager
        val installedPkgs = pkgManager.getInstalledPackages(PACKAGE_FLAGS)
        val extPkgs = installedPkgs.filter { isPackageAnExtension(it) }

        if (extPkgs.isEmpty()) return emptyList()

        // Load each extension concurrently and wait for completion
        return runBlocking {
            val deferred = extPkgs.map {
                async { loadExtension(context, it.packageName, it) }
            }
            deferred.map { it.await() }
        }
    }

    /**
     * Attempts to load an extension from the given package name. It checks if the extension
     * contains the required feature flag before trying to load it.
     */
    fun loadExtensionFromPkgName(context: Context, pkgName: String): LoadResult {
        val pkgInfo = try {
            context.packageManager.getPackageInfo(pkgName, PACKAGE_FLAGS)
        } catch (error: PackageManager.NameNotFoundException) {
            // Unlikely, but the package may have been uninstalled at this point
            return LoadResult.Error(error)
        }
        if (!isPackageAnExtension(pkgInfo)) {
            return LoadResult.Error("Tried to load a package that wasn't a extension")
        }
        return loadExtension(context, pkgName, pkgInfo)
    }

    /**
     * Loads an extension given its package name.
     *
     * @param context The application context.
     * @param pkgName The package name of the extension to load.
     * @param pkgInfo The package info of the extension.
     */
    private fun loadExtension(context: Context, pkgName: String, pkgInfo: PackageInfo): LoadResult {
        val pkgManager = context.packageManager

        val appInfo = try {
            pkgManager.getApplicationInfo(pkgName, PackageManager.GET_META_DATA)
        } catch (error: PackageManager.NameNotFoundException) {
            // Unlikely, but the package may have been uninstalled at this point
            return LoadResult.Error(error)
        }

        val extName = pkgManager.getApplicationLabel(appInfo)?.toString()
            .orEmpty().substringAfter("Tachiyomi: ")
        val versionName = pkgInfo.versionName
        val versionCode = pkgInfo.versionCode

        if (versionName.isNullOrEmpty()) {
            val exception = Exception("Missing versionName for extension $extName")
            Timber.w(exception)
            return LoadResult.Error(exception)
        }

        // Validate lib version
        val libVersion = versionName.substringBeforeLast('.').toDouble()
        if (libVersion < LIB_VERSION_MIN || libVersion > LIB_VERSION_MAX) {
            val exception = Exception("Lib version is $libVersion, while only versions " +
                    "$LIB_VERSION_MIN to $LIB_VERSION_MAX are allowed")
            Timber.w(exception)
            return LoadResult.Error(exception)
        }

        val signatureHash = getSignatureHash(pkgInfo)

        if (signatureHash == null) {
            return LoadResult.Error("Package $pkgName isn't signed")
        } else if (signatureHash !in trustedSignatures) {
            val extension = Extension.Untrusted(extName, pkgName, versionName, versionCode, signatureHash)
            Timber.w("Extension $pkgName isn't trusted")
            return LoadResult.Untrusted(extension)
        }

        val classLoader = PathClassLoader(appInfo.sourceDir, null, context.classLoader)

        val sources = appInfo.metaData.getString(METADATA_SOURCE_CLASS)!!
                .split(";")
                .map {
                    val sourceClass = it.trim()
                    if (sourceClass.startsWith("."))
                        pkgInfo.packageName + sourceClass
                    else
                        sourceClass
                }
                .flatMap {
                    try {
                        val obj = Class.forName(it, false, classLoader).newInstance()
                        when (obj) {
                            is Source -> listOf(obj)
                            is SourceFactory -> obj.createSources()
                            else -> throw Exception("Unknown source class type! ${obj.javaClass}")
                        }
                    } catch (e: Throwable) {
                        Timber.e(e, "Extension load error: $extName.")
                        return LoadResult.Error(e)
                    }
                }
        val langs = sources.filterIsInstance<CatalogueSource>()
                .map { it.lang }
                .toSet()

        val lang = when (langs.size) {
            0 -> ""
            1 -> langs.first()
            else -> "all"
        }

        val extension = Extension.Installed(extName, pkgName, versionName, versionCode, sources, lang)
        return LoadResult.Success(extension)
    }

    /**
     * Returns true if the given package is an extension.
     *
     * @param pkgInfo The package info of the application.
     */
    private fun isPackageAnExtension(pkgInfo: PackageInfo): Boolean {
        return pkgInfo.reqFeatures.orEmpty().any { it.name == EXTENSION_FEATURE }
    }

    /**
     * Returns the signature hash of the package or null if it's not signed.
     *
     * @param pkgInfo The package info of the application.
     */
    private fun getSignatureHash(pkgInfo: PackageInfo): String? {
        val signatures = pkgInfo.signatures
        return if (signatures != null && !signatures.isEmpty()) {
            Hash.sha256(signatures.first().toByteArray())
        } else {
            null
        }
    }

}
