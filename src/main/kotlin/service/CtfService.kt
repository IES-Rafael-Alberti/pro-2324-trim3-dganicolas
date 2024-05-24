package service


import dao.iDao.IDaoCtf
import dataclassEntity.Ctfs

class CtfService(private val ctfDao: IDaoCtf):ICtfsService {
    override fun anadirParticipacion(ctf: Ctfs): Ctfs? {
        //llamo al dao ctf para añadir una participacion
        return ctfDao.anadirParticipacion(ctf)
    }

    override fun escogerListaCtfOParteDeCtf(id: Int?): List<Ctfs>? {
        //escogo los ctf existentes o una parte de ellos
        return ctfDao.getAll(id)
    }

    override fun escogerUnSoloCtf(id: Int): Ctfs? {
        //escogo un solo ctfw
        return ctfDao.selectById(id)
    }

    override fun eliminarParticipacion(id: Int):Boolean {
        //elimino una participacion
        return ctfDao.eliminarParticipacion(id)
    }

    override fun eliminarParticipacion(id: Int, idctf: Int):Boolean {
        //elimino una sola pariticipacion de un grupo
        return ctfDao.eliminarParticipacionDeUnGrupoEnUnCtf(id,idctf)
    }

    override fun getAll(id: Int?): List<Ctfs>? {
        //escogo los ctf
        return ctfDao.getAll(id)
    }

    override fun actualizarPuntuacion(ctf: Ctfs): Ctfs? {
        //actualizo las puntuaciones de los ctfs
        return ctfDao.actualizarPuntuacion(ctf)
    }

    override fun comprobarExistencia(ctf: Ctfs): Boolean {
        //compruebo la existencia de un ctf
        return ctfDao.comprobarExistencia(ctf)
    }

    override fun mostrarInformacionGrupo(id: Int?): List<Ctfs>? {
        //muestro la info de un grupo
        return ctfDao.mostrarInformacionGrupo(id)
    }
}