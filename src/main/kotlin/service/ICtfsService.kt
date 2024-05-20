package service

import dataclass.Ctfs

interface ICtfsService {
    fun anadirParticipacion(ctf: Ctfs): Ctfs?
    fun escogerListaCtfOParteDeCtf(id: Int? = null): List<Ctfs>?
    fun escogerUnSoloCtf(id: Int): Ctfs?
    fun update(ctfs: Ctfs): Ctfs?
    fun eliminarParticipacion(id: Int):Boolean
    fun eliminarParticipacion(id: Int,idctf:Int): Boolean
    fun getAll(id:Int? = null): List<Ctfs>?
    fun actualizarPuntuacion(ctf: Ctfs): Ctfs?
    fun comprobarExistencia(ctf: Ctfs): Boolean
}