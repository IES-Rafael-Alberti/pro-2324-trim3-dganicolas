package DAO.XmlDao

import DAO.IDAO.IDaoCtf
import dataclassEntity.Ctfs

class XmlCtfDao: IDaoCtf {
    override fun anadirParticipacion(ctf: Ctfs): Ctfs? {
        TODO("anado una nueva participacion")
    }

    override fun getAll(numero: Int?): List<Ctfs>? {
        TODO("cogo todos los ctf")
    }

    override fun selectById(id: Int): Ctfs? {
        TODO("selecciono un solo ctf")
    }

    override fun eliminarParticipacion(id: Int): Boolean {
        TODO("elimino una participacion")
    }

    override fun eliminarParticipacionDeUnGrupoEnUnCtf(id: Int, ctfid: Int): Boolean {
        TODO("elimino una participacion de un grupo")
    }

    override fun comprobarExistencia(ctf: Ctfs): Boolean {
        TODO("miro si existe esa participacion")
    }

    override fun actualizarPuntuacion(ctf: Ctfs): Ctfs? {
        TODO("actualizo la puntiacion de un ctf")
    }

    override fun mostrarInformacionGrupo(id: Int?): List<Ctfs>? {
        TODO("muestro la info de un grupo en un ctf")
    }
}