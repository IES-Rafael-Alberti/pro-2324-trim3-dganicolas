package service

import DAO.IDaoCtf
import dataclass.Ctfs

class CtfService(private val ctfDao:IDaoCtf):ICtfsService {
    override fun anadirParticipacion(ctf: Ctfs): Ctfs? {
        TODO("Not yet implemented")
    }

    override fun getById(id: Int): Ctfs? {
        return ctfDao.selectById(id)

    }

    override fun update(ctfs: Ctfs): Ctfs? {
        TODO("Not yet implemented")
    }

    override fun eliminarParticipacion(id: Int) {
        TODO("Not yet implemented")
    }

    override fun eliminarParticipacion(id: Int, idctf: Int):Boolean {
        return ctfDao.eliminarParticipacionDeUnGrupoEnUnCtf(id,idctf)
    }

    override fun getAll(id: Int?): List<Ctfs>? {
        return ctfDao.getAll(id)
    }

    override fun actualizarPuntuacion(ctf: Ctfs): Ctfs? {
        TODO("Not yet implemented")
    }

    override fun comprobarExistencia(ctf: Ctfs): Boolean {
        TODO("Not yet implemented")
    }
}