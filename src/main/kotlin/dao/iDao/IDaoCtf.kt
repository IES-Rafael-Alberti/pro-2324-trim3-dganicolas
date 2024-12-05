package dao.iDao

import dataclassEntity.Ctfs

interface IDaoCtf {

    fun anadirParticipacion(ctf: Ctfs): Ctfs?
    fun getAll(numero:Int?= null): List<Ctfs>?
    fun selectById(id: Int): Ctfs?
    fun eliminarParticipacion(id: Int): Boolean
    fun eliminarParticipacionDeUnGrupoEnUnCtf(id: Int, ctfid:Int): Boolean
    fun comprobarExistencia(ctf: Ctfs): Boolean
    fun actualizarPuntuacion(ctf: Ctfs): Ctfs?
    fun mostrarInformacionGrupo(id: Int? =null): List<Ctfs>?
}
