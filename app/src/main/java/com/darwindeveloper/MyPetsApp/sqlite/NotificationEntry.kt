package com.darwindeveloper.MyPetsApp.sqlite

/**
 * Created by DARWIN MOROCHO on 4/9/2017.
 */
object NotificationEntry {
    val TABLE_NAME = "notificaciones"
    val TABLE_VERSION = 2
    val SQL_CREATE = "create table $TABLE_NAME ( id INTEGER PRIMARY KEY AUTOINCREMENT," +
            " remitente_id TEXT NOT NULL, remitente TEXT NOT NULL, " +
            " html TEXT NOT NULL, titulo TEXT NOT NULL, created_at TEXT NOT NULL, tipo TEXT NOT NULL)"

    val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME
}