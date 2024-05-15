package DAO

import dataclass.Ctfs

interface IDaoCtf {

    fun insert(ctf: Ctfs): Ctfs?
    fun getAll(numero:Int?= null): List<Ctfs>?
    fun selectById(id: Int): Ctfs?
    fun update(book: Ctfs): Ctfs?
    fun eliminarParticipacion(id: Int): Boolean
    fun eliminarParticipacionDeUnGrupoEnUnCtf(id: Int, ctfid:Int): Boolean
    fun comprobarExistencia(ctf: Ctfs): Boolean
    fun actualizarPuntuacion(ctf: Ctfs): Ctfs?
}
