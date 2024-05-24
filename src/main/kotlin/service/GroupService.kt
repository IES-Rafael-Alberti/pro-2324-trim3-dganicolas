package service

import dao.iDao.IDaoGroup
import dataclassEntity.Ctfs
import dataclassEntity.Grupos


class GroupService(private val groupDao: IDaoGroup) :IGruposService {
    override fun crearGrupo(grupo: Grupos): Grupos? {
        //creo grupo
        return groupDao.crearGrupo(grupo)
    }

    override fun getById(id: Int): Grupos? {
        //escogo un grupo por su id
        return groupDao.selectById(id)
    }

    override fun eliminarCtf(grupo: Grupos): Grupos? {
        //elimino un grupo
        return groupDao.eliminarCtf(grupo)
    }

    override fun verificargrupo(grupo: Grupos): Boolean {
        //verifico si existe
        return groupDao.verificarGrupo(grupo)
    }

    override fun eliminarGrupo(id: Int) {
        //elimino un grupo
        groupDao.eliminarGrupo(id)
    }

    override fun getAll(): List<Grupos>? {
        //escogo los grupos
        return groupDao.getAll()
    }

    override fun actualizarmejorCtfs(grupo: Grupos, ctfs: List<Ctfs>?):Grupos? {
        //actualizo el campo mejor ctf id del grupo
        return groupDao.actualizarPosiciones(grupo,ctfs)
    }

    override fun mostrarInformacionGrupo(id: Int?): List<Grupos>? {
        //muestro info de un grupo
        return groupDao.mostrarInformacionGrupo(id)
    }
}