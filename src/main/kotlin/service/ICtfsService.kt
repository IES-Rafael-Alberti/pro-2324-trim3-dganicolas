package service

import dataclass.Ctfs

interface ICtfsService {
    fun anadirParticipacion(ctf: Ctfs): Ctfs?
    fun getById(id: Int): Ctfs?
    fun update(ctfs: Ctfs): Ctfs?
    fun eliminarParticipacion(id: Int)
    fun eliminarParticipacion(id: Int,idctf:Int): Boolean
    fun getAll(id:Int? = null): List<Ctfs>?
    fun actualizarPuntuacion(ctf: Ctfs): Ctfs?
    fun comprobarExistencia(ctf: Ctfs): Boolean
}