package service

import dataclassEntity.Ctfs

interface ICtfsService {
    fun anadirParticipacion(ctf: Ctfs): Ctfs?
    fun escogerListaCtfOParteDeCtf(id: Int? = null): List<Ctfs>?
    fun escogerUnSoloCtf(id: Int): Ctfs?
    fun eliminarParticipacion(id: Int):Boolean
    fun eliminarParticipacion(id: Int,idctf:Int): Boolean
    fun getAll(id:Int? = null): List<Ctfs>?
    fun actualizarPuntuacion(ctf: Ctfs): Ctfs?
    fun comprobarExistencia(ctf: Ctfs): Boolean
    fun mostrarInformacionGrupo(Id: Int? = null): List<Ctfs>?
}