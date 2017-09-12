package com.darwindeveloper.MyPetsApp.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import android.widget.Toast
import java.util.*


/**
 * Created by DARWIN MOROCHO on 4/9/2017.
 */
class DBManager(private val context: Context) {

    private var db: SQLiteDatabase? = null

    init {
        db = DBHelper(context).writableDatabase
    }


    public fun getAll(): ArrayList<Notification> {
        val list = ArrayList<Notification>()

        val cursor = db?.rawQuery("select * from ${NotificationEntry.TABLE_NAME} order by created_at desc", null)




        if (cursor!!.count > 0) {
            cursor.moveToFirst()

            do {

                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val titulo = cursor.getString(cursor.getColumnIndex("titulo"))
                val remitente_id = cursor.getString(cursor.getColumnIndex("remitente_id"))
                val remitente = cursor.getString(cursor.getColumnIndex("remitente"))
                val html = cursor.getString(cursor.getColumnIndex("html"))
                val created_at = cursor.getString(cursor.getColumnIndex("created_at"))

                list.add(Notification(id, titulo, remitente_id, remitente, html, created_at))

            } while (cursor.moveToNext())

        }

        cursor.close()

        return list
    }


    public fun save(titulo: String, remitente_id: String, remitente: String, html: String, fecha: String) {

        val values = ContentValues()

        values.put("remitente_id", remitente_id)
        values.put("remitente", remitente)

        values.put("html", html)
        values.put("titulo", titulo)
        values.put("created_at", fecha)

        // val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime())


        val id = db?.insert(NotificationEntry.TABLE_NAME, null, values)
        if ("$id" != "-1") {
            Toast.makeText(context, "Guardado con exito", Toast.LENGTH_SHORT).show()
        } else
            Toast.makeText(context, "Error intente nuevamente", Toast.LENGTH_SHORT).show()


    }


    public fun delete(id: String): Int {
        val ars = arrayOf(id);
        return db!!.delete(NotificationEntry.TABLE_NAME, "id = ? ", ars);
    }

}