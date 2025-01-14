package eu.kanade.t4chiyomi.util

import android.content.Context
import android.net.Uri
import android.os.Build
import android.support.v4.content.FileProvider
import eu.kanade.t4chiyomi.BuildConfig
import java.io.File

/**
 * Returns the uri of a file
 *
 * @param context context of application
 */
fun File.getUriCompat(context: Context): Uri {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", this)
    else Uri.fromFile(this)
}

