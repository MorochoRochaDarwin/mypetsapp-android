package com.darwindeveloper.MyPetsApp.sqlite

/**
 * Created by DARWIN MOROCHO on 23/9/2017.
 */
object EstEntry {
    val TABLE_NAME = "establecimientos"


    val SQL_CREATE = "create table $TABLE_NAME ( establecimiento_id TEXT PRIMARY KEY," +
            " codigo TEXT NOT NULL, nombre_establecimiento TEXT NOT NULL, " +
            " user_id TEXT NOT NULL, tipo TEXT NOT NULL, icono TEXT, provincia TEXT , ciudad TEXT, sector TEXT, addr TEXT)"

    val SQL_DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME
}