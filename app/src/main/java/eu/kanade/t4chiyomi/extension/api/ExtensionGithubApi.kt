package eu.kanade.t4chiyomi.extension.api

import com.github.salomonbrys.kotson.fromJson
import com.github.salomonbrys.kotson.get
import com.github.salomonbrys.kotson.int
import com.github.salomonbrys.kotson.string
import com.google.gson.Gson
import com.google.gson.JsonArray
import eu.kanade.t4chiyomi.extension.model.Extension
import eu.kanade.t4chiyomi.extension.util.ExtensionLoader
import eu.kanade.t4chiyomi.network.GET
import eu.kanade.t4chiyomi.network.NetworkHelper
import eu.kanade.t4chiyomi.network.asObservableSuccess
import okhttp3.Response
import rx.Observable
import uy.kohesive.injekt.injectLazy

internal class ExtensionGithubApi {

    private val network: NetworkHelper by injectLazy()

    private val client get() = network.client

    private val gson: Gson by injectLazy()

    private val repoUrl = "https://raw.githubusercontent.com/TagnumElite/tachiyomi-extensions/repo"

    fun findExtensions(): Observable<List<Extension.Available>> {
        val call = GET("$repoUrl/index.json")

        return client.newCall(call).asObservableSuccess()
                .map(::parseResponse)
    }

    private fun parseResponse(response: Response): List<Extension.Available> {
        val text = response.body()?.use { it.string() } ?: return emptyList()

        val json = gson.fromJson<JsonArray>(text)

        return json
                .filter { element ->
                    val versionName = element["version"].string
                    val libVersion = versionName.substringBeforeLast('.').toDouble()
                    libVersion >= ExtensionLoader.LIB_VERSION_MIN && libVersion <= ExtensionLoader.LIB_VERSION_MAX
                }
                .map { element ->
                    val name = element["name"].string.substringAfter("Tachiyomi: ")
                    val pkgName = element["pkg"].string
                    val apkName = element["apk"].string
                    val versionName = element["version"].string
                    val versionCode = element["code"].int
                    val lang = element["lang"].string
                    val icon = "$repoUrl/icon/${apkName.replace(".apk", ".png")}"

                    Extension.Available(name, pkgName, versionName, versionCode, lang, apkName, icon)
                }
    }

    fun getApkUrl(extension: Extension.Available): String {
        return "$repoUrl/apk/${extension.apkName}"
    }
}
