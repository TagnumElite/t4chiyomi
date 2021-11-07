package eu.kanade.t4chiyomi.data.database

import com.pushtorefresh.storio.sqlite.impl.DefaultStorIOSQLite

interface DbProvider {

    val db: DefaultStorIOSQLite

}