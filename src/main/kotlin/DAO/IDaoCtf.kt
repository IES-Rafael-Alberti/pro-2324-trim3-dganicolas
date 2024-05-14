package DAO

import dataclass.Ctfs

interface IDaoCtf {

    fun getAll(numero:Int?= null): List<Ctfs>?
    fun selectByIdCtf(id: Int): Ctfs?
    fun update(book: Ctfs): Ctfs?
    fun deleteById(id: Int): Boolean
    fun anadirParticipacion(Ctf: Ctfs): Ctfs?
    fun comprobarExistencia(ctf: Ctfs): Boolean
    fun actualizarPuntuacion(ctf: Ctfs): Ctfs?
}
