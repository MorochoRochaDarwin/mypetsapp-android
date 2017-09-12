package com.darwindeveloper.MyPetsApp.sqlite

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * Created by DARWIN MOROCHO on 4/9/2017.
 */
class DBHelper(context: Context) : SQLiteOpenHelper(context, NotificationEntry.TABLE_NAME, null, NotificationEntry.TABLE_VERSION) {


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(NotificationEntry.SQL_CREATE)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL(NotificationEntry.SQL_DELETE_TABLE);//eliminamos la tabla usuarios
        onCreate(db);
    }


    override fun onDowngrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        onUpgrade(db, oldVersion, newVersion)
    }


}
