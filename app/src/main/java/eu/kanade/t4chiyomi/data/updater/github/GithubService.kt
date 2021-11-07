package eu.kanade.t4chiyomi.data.updater.github

import eu.kanade.t4chiyomi.network.NetworkHelper
import retrofit2.Retrofit
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import rx.Observable
import uy.kohesive.injekt.Injekt
import uy.kohesive.injekt.api.get

/**
 * Used to connect with the Github API.
 * TODO: Replace this with my own releases
 */
interface GithubService {

    companion object {
        fun create(): GithubService {
            val restAdapter = Retrofit.Builder()
                    .baseUrl("https://api.github.com")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(Injekt.get<NetworkHelper>().client)
                    .build()

            return restAdapter.create(GithubService::class.java)
        }
    }

    @GET("/repos/TagnumElite/tachiyomi/releases/latest")
    fun getLatestVersion(): Observable<GithubRelease>

}
