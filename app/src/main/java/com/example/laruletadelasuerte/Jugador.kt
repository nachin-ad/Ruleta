package com.example.laruletadelasuerte

import android.os.Parcel
import android.os.Parcelable

data class Jugador (
    val nombre: String,
    var dineroActual: Int,
    var dineroTotal: Int,
    var imagenResId: Int
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nombre)
        parcel.writeInt(dineroActual)
        parcel.writeInt(dineroTotal)
        parcel.writeInt(imagenResId)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Jugador> {
        override fun createFromParcel(parcel: Parcel): Jugador {
            return Jugador(parcel)
        }

        override fun newArray(size: Int): Array<Jugador?> {
            return arrayOfNulls(size)
        }
    }
}