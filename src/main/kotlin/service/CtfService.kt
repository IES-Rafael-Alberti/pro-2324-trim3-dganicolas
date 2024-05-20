package service

import DAO.IDaoCtf
import dataclass.Ctfs

class CtfService(private val ctfDao:IDaoCtf):ICtfsService {
    override fun anadirParticipacion(ctf: Ctfs): Ctfs? {
        return ctfDao.anadirParticipacion(ctf)
    }

    override fun escogerListaCtfOParteDeCtf(id: Int?): List<Ctfs>? {
        return ctfDao.getAll(id)

    }

    override fun escogerUnSoloCtf(id: Int): Ctfs? {
        return ctfDao.selectById(id)
    }


    override fun update(ctfs: Ctfs): Ctfs? {
        TODO("Not yet implemented")
    }

    override fun eliminarParticipacion(id: Int):Boolean {
        return ctfDao.eliminarParticipacion(id)
    }

    override fun eliminarParticipacion(id: Int, idctf: Int):Boolean {
        return ctfDao.eliminarParticipacionDeUnGrupoEnUnCtf(id,idctf)
    }

    override fun getAll(id: Int?): List<Ctfs>? {
        //si el numero viene nulo me retorna todos los registros de ctf
        //esta funcion me funciona sin testear
        return ctfDao.getAll(id)
    }

    override fun actualizarPuntuacion(ctf: Ctfs): Ctfs? {
        return ctfDao.actualizarPuntuacion(ctf)
    }

    override fun comprobarExistencia(ctf: Ctfs): Boolean {
        TODO("Not yet implemented")
    }
}