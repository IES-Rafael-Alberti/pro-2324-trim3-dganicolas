package service

import DAO.IDaoGroup
import dataclassEntity.Ctfs
import dataclassEntity.Grupos

class GroupService(private val groupDao: IDaoGroup) :IGruposService {
    override fun crearGrupo(grupo: Grupos): Grupos? {
        //llamo a la funcion de crear grupo
        return groupDao.crearGrupo(grupo)
    }

    override fun getById(id: Int): Grupos? {
        return groupDao.selectById(id)
    }

    override fun eliminarCtf(grupo: Grupos): Grupos? {
        return groupDao.eliminarCtf(grupo)
    }

    override fun verificargrupo(grupo: Grupos): Boolean {
        return groupDao.verificarGrupo(grupo)
    }

    override fun eliminarGrupo(id: Int) {
        groupDao.eliminarGrupo(id)
    }

    override fun getAll(): List<Grupos>? {
        return groupDao.getAll()
    }

    override fun actualizarmejorCtfs(grupo: Grupos, ctfs: List<Ctfs>?):Grupos? {
        return groupDao.actualizarPosiciones(grupo,ctfs)
    }

    override fun mostrarInformacionGrupo(id: Int?): List<Grupos>? {
        return groupDao.mostrarInformacionGrupo(id)
    }
}