package DAO

import dataclass.Ctfs
import dataclass.Grupos

interface Dao {
    fun insert(grupo: Grupos): Grupos?
    fun getAll(): List<Grupos>?
    fun getAllCtf(): List<Ctfs>?
    fun selectById(id: Int): Grupos?
    fun selectByIdCtf(id: Int): Ctfs?
    fun update(book: Grupos): Grupos?
    fun update(book: Ctfs): Ctfs?
    fun deleteById(id:Int): Boolean
    fun deleteByIdCtf(id:Int): Boolean
    fun anadirParticipacion(Ctf: Ctfs): Ctfs?
    fun comprobarExistencia(ctf: Ctfs): Boolean
    fun actualizarPuntuacion(ctf: Ctfs): Ctfs?
}