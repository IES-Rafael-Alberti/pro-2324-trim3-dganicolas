package service

import DAO.IDaoGroup
import dataclass.Ctfs
import dataclass.Grupos

class GroupService(private val groupDao: IDaoGroup) :IGruposService {
    override fun crearGrupo(grupo: Grupos): Grupos? {
        //llamo a la funcion de crear grupo
        return groupDao.crearGrupo(grupo)
    }

    override fun getById(id: Int): Grupos? {
        TODO("Not yet implemented")
    }

    override fun update(grupo: Grupos): Grupos? {
        TODO("Not yet implemented")
    }

    override fun eliminarGrupo(id: Int) {
        groupDao.eliminarGrupo(id)
    }

    override fun getAll(): List<Grupos>? {
        TODO("Not yet implemented")
    }

    override fun actualizarmejorCtfs(grupo: Grupos, ctfs: List<Ctfs>?):Grupos? {
        return groupDao.actualizarPosiciones(grupo,ctfs)
    }
}