package DAO

import dataclass.Ctfs
import dataclass.Grupos

interface IDaoCtf {

    fun insert(Ctf: Ctfs): Ctfs?
    fun getAll(numero:Int?= null): List<Ctfs>?
    fun selectById(id: Int): Ctfs?
    fun update(book: Ctfs): Ctfs?
    fun deleteById(id: Int): Boolean
    fun comprobarExistencia(ctf: Ctfs): Boolean
    fun actualizarPuntuacion(ctf: Ctfs): Ctfs?
}
