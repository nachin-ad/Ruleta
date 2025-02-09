package com.example.laruletadelasuerte

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
//clase que controla la base de datos
class Historial(context: Context) : SQLiteOpenHelper(context, NOMBRE_BASEDEDATOS, null, VERSION_BASEDEDATOS) {
    //asignamos todas las variables necesarias para el manejo de los datos
    companion object {
        private const val NOMBRE_BASEDEDATOS = "historial.db"
        private const val VERSION_BASEDEDATOS = 1

        const val NOMBRE_TABLA = "historial_tb"
        const val ID_COLUMNA = "id"
        const val NOMBRE_JUGADOR1 = "nombre_jugador1"
        const val DINERO_JUGADOR1 = "dinero_jugador1"
        const val NOMBRE_JUGADOR2 = "nombre_jugador2"
        const val DINERO_JUGADOR2 = "dinero_jugador2"
        const val NOMBRE_JUGADOR3 = "nombre_jugador3"
        const val DINERO_JUGADOR3 = "dinero_jugador3"
        const val GANADOR = "ganador"
        const val FECHA = "fecha"
    }
    //creacion de la tabla donde se guardará la información
    override fun onCreate(db: SQLiteDatabase) {
        val createTableQuery = """
            CREATE TABLE $NOMBRE_TABLA (
                $ID_COLUMNA INTEGER PRIMARY KEY AUTOINCREMENT,
                $NOMBRE_JUGADOR1 TEXT,
                $DINERO_JUGADOR1 INTEGER,
                $NOMBRE_JUGADOR2 TEXT,
                $DINERO_JUGADOR2 INTEGER,
                $NOMBRE_JUGADOR3 TEXT,
                $DINERO_JUGADOR3 INTEGER,
                $GANADOR TEXT,
                $FECHA TEXT
            )
        """.trimIndent()
        db.execSQL(createTableQuery)
    }
    //funcion para actualizar la tabla
    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $NOMBRE_TABLA")
        onCreate(db)
    }
    //funcion para guardar la partida
    fun guardarPartida(
        nombreJugador1: String, dineroJugador1: Int,
        nombreJugador2: String, dineroJugador2: Int,
        nombreJugador3: String, dineroJugador3: Int,
        ganador: String, fecha: String
    ) {
        val db = this.writableDatabase
        val values = ContentValues().apply {
            put(NOMBRE_JUGADOR1, nombreJugador1)
            put(DINERO_JUGADOR1, dineroJugador1)
            put(NOMBRE_JUGADOR2, nombreJugador2)
            put(DINERO_JUGADOR2, dineroJugador2)
            put(NOMBRE_JUGADOR3, nombreJugador3)
            put(DINERO_JUGADOR3, dineroJugador3)
            put(GANADOR, ganador)
            put(FECHA, fecha)
        }
        db.insert(NOMBRE_TABLA, null, values)
        db.close()
    }
//funcion para obtener los datos que se mostrarán en el historial
    fun obtenerHistorial(): List<Partida> {
        val partidas = mutableListOf<Partida>()
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $NOMBRE_TABLA", null) // ✅ CORREGIDO

        while (cursor.moveToNext()) {
            val partida = Partida(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(ID_COLUMNA)),
                nombreJugador1 = cursor.getString(cursor.getColumnIndexOrThrow(NOMBRE_JUGADOR1)),
                dineroJugador1 = cursor.getInt(cursor.getColumnIndexOrThrow(DINERO_JUGADOR1)),
                nombreJugador2 = cursor.getString(cursor.getColumnIndexOrThrow(NOMBRE_JUGADOR2)),
                dineroJugador2 = cursor.getInt(cursor.getColumnIndexOrThrow(DINERO_JUGADOR2)),
                nombreJugador3 = cursor.getString(cursor.getColumnIndexOrThrow(NOMBRE_JUGADOR3)),
                dineroJugador3 = cursor.getInt(cursor.getColumnIndexOrThrow(DINERO_JUGADOR3)),
                ganador = cursor.getString(cursor.getColumnIndexOrThrow(GANADOR)),
                fecha = cursor.getString(cursor.getColumnIndexOrThrow(FECHA))
            )
            partidas.add(partida)
        }
        cursor.close()
        db.close()
        return partidas
    }
    //funcion que controla si el historial contiene datos
    fun isHistorialVacio(): Boolean{
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT COUNT(*) FROM $NOMBRE_TABLA", null)
        var count = 0

        if (cursor.moveToFirst()){
            count = cursor.getInt(0)
        }
        cursor.close()
        db.close()

        return count == 0
    }
    // funcion para obtener los datos de la ultima partida jugada
    fun obtenerUltimaPartida(): Partida? {
        val db = this.readableDatabase
        val cursor = db.rawQuery("SELECT * FROM $NOMBRE_TABLA ORDER BY $FECHA DESC LIMIT 1", null)

        return if (cursor.moveToFirst()) {
            val partida = Partida(
                id = cursor.getInt(cursor.getColumnIndexOrThrow(ID_COLUMNA)),
                nombreJugador1 = cursor.getString(cursor.getColumnIndexOrThrow(NOMBRE_JUGADOR1)),
                dineroJugador1 = cursor.getInt(cursor.getColumnIndexOrThrow(DINERO_JUGADOR1)),
                nombreJugador2 = cursor.getString(cursor.getColumnIndexOrThrow(NOMBRE_JUGADOR2)),
                dineroJugador2 = cursor.getInt(cursor.getColumnIndexOrThrow(DINERO_JUGADOR2)),
                nombreJugador3 = cursor.getString(cursor.getColumnIndexOrThrow(NOMBRE_JUGADOR3)),
                dineroJugador3 = cursor.getInt(cursor.getColumnIndexOrThrow(DINERO_JUGADOR3)),
                ganador = cursor.getString(cursor.getColumnIndexOrThrow(GANADOR)),
                fecha = cursor.getString(cursor.getColumnIndexOrThrow(FECHA))
            )
            cursor.close()
            db.close()
            partida
        } else {
            cursor.close()
            db.close()
            null
        }
    }
}