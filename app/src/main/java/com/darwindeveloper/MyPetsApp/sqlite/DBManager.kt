package com.darwindeveloper.MyPetsApp.sqlite

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import android.widget.Toast
import com.darwindeveloper.MyPetsApp.api.modelos.Establecimiento
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
                val tipo = cursor.getString(cursor.getColumnIndex("tipo"))
                val remitente_id = cursor.getString(cursor.getColumnIndex("remitente_id"))
                val remitente = cursor.getString(cursor.getColumnIndex("remitente"))
                val html = cursor.getString(cursor.getColumnIndex("html"))
                val created_at = cursor.getString(cursor.getColumnIndex("created_at"))

                list.add(Notification(id, titulo, tipo, remitente_id, remitente, html, created_at))

            } while (cursor.moveToNext())

        }

        cursor.close()

        return list
    }


    public fun getQhere(where: String): ArrayList<Notification> {
        val list = ArrayList<Notification>()

        val cursor = db?.rawQuery("select * from ${NotificationEntry.TABLE_NAME} where $where order by created_at desc", null)




        if (cursor!!.count > 0) {
            cursor.moveToFirst()

            do {
                val id = cursor.getInt(cursor.getColumnIndex("id"))
                val titulo = cursor.getString(cursor.getColumnIndex("titulo"))
                val tipo = cursor.getString(cursor.getColumnIndex("tipo"))
                val remitente_id = cursor.getString(cursor.getColumnIndex("remitente_id"))
                val remitente = cursor.getString(cursor.getColumnIndex("remitente"))
                val html = cursor.getString(cursor.getColumnIndex("html"))
                val created_at = cursor.getString(cursor.getColumnIndex("created_at"))

                list.add(Notification(id, titulo, tipo, remitente_id, remitente, html, created_at))

            } while (cursor.moveToNext())

        }

        cursor.close()

        return list
    }


    public fun save(titulo: String, tipo: String, remitente_id: String, remitente: String, html: String, fecha: String) {

        val values = ContentValues()

        values.put("remitente_id", remitente_id)
        values.put("remitente", remitente)

        values.put("html", html)
        values.put("titulo", titulo)
        values.put("tipo", tipo)
        values.put("created_at", fecha)
        values.put("visto", "false")

        // val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime())


        val id = db?.insert(NotificationEntry.TABLE_NAME, null, values)
        if ("$id" != "-1") {
            Log.i("sqlsave", "Guardado con exito")
        } else {
            Log.i("sqlsave", "Guardado error")
        }


    }


    public fun delete(id: String): Int {
        val ars = arrayOf(id);
        return db!!.delete(NotificationEntry.TABLE_NAME, "id = ? ", ars);
    }


    public fun saveEst(establecimiento: Establecimiento): Int {
        val values = ContentValues()

        values.put("establecimiento_id", establecimiento.establecimiento_id)
        values.put("nombre_establecimiento", establecimiento.nombre_establecimiento)
        values.put("codigo", establecimiento.codigo)
        values.put("user_id", establecimiento.user_id)
        values.put("icono", establecimiento.icono)
        values.put("tipo", establecimiento.tipo)
        values.put("provincia", establecimiento.provincia)
        values.put("ciudad", establecimiento.ciudad)
        values.put("sector", establecimiento.sector)


        // val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss").format(Calendar.getInstance().getTime())


        val id = db?.insert(EstEntry.TABLE_NAME, null, values)
        if ("$id" != "-1") {
            return 1
        } else {
            return -1
        }


    }

    public fun getEst(): ArrayList<Establecimiento> {
        val list = ArrayList<Establecimiento>()

        val cursor = db?.rawQuery("select * from ${EstEntry.TABLE_NAME}", null)




        if (cursor!!.count > 0) {
            cursor.moveToFirst()

            do {

                val id = cursor.getString(cursor.getColumnIndex("establecimiento_id"))
                val nombre = cursor.getString(cursor.getColumnIndex("nombre_establecimiento"))
                val tipo = cursor.getString(cursor.getColumnIndex("tipo"))
                val codigo = cursor.getString(cursor.getColumnIndex("codigo"))
                val user_id = cursor.getString(cursor.getColumnIndex("user_id"))
                val icono = cursor.getString(cursor.getColumnIndex("icono"))
                val provincia = cursor.getString(cursor.getColumnIndex("provincia"))
                val ciudad = cursor.getString(cursor.getColumnIndex("ciudad"))
                val sector = cursor.getString(cursor.getColumnIndex("sector"))

                val est = Establecimiento()
                est.establecimiento_id = id
                est.nombre_establecimiento = nombre
                est.tipo = tipo
                est.codigo = codigo
                est.user_id = user_id
                est.icono = icono
                est.provincia = provincia
                est.ciudad = ciudad
                est.sector = sector


                list.add(est)

            } while (cursor.moveToNext())

        }

        cursor.close()

        return list

    }


    public fun getNums(): Array<Int> {


        val arr = arrayOf(0, 0, 0, 0, 0)


        val sql = "select * from ${NotificationEntry.TABLE_NAME} where tipo='Ultimas noticias' and visto='false'"
        val c = db!!.rawQuery(sql, null)
        arr[3] = c.count
        c.close()

        val sql2 = "select * from ${NotificationEntry.TABLE_NAME} where tipo='Descuentos' and visto='false'"
        val c2 = db!!.rawQuery(sql2, null)
        arr[1] = c2.count
        c2.close()

        val sql3 = "select * from ${NotificationEntry.TABLE_NAME} where tipo='Publicidad' and visto='false'"
        val c3 = db!!.rawQuery(sql3, null)
        arr[2] = c3.count
        c3.close()


        val sql4 = "select * from ${NotificationEntry.TABLE_NAME} where tipo='Veterinario' and visto='false'"
        val c4 = db!!.rawQuery(sql4, null)
        arr[0] = c4.count
        c4.close()

        val sql5 = "select * from ${NotificationEntry.TABLE_NAME} where tipo!='Veterinario' and tipo!='Publicidad' and tipo!='Descuentos' and tipo!='Ultimas noticias' and visto='false'"
        val c5 = db!!.rawQuery(sql4, null)
        arr[4] = c5.count
        c5.close()


        return arr

    }


    public fun visto(id: Int) {

        val v = ContentValues()
        v.put("visto", "true")
        db!!.update(NotificationEntry.TABLE_NAME, v, "id=$id", null)
    }

}