package service

import DAO.IDaoGroup
import DAO.SqlDao.SqlDaoGroup
import dataclass.Grupos

class GroupService(private val groupDao: IDaoGroup) :IGruposService {
    override fun crearGrupo(grupo: Grupos): Grupos? {
        return groupDao.insert(grupo)
    }

    override fun getById(id: Int): Grupos? {
        TODO("Not yet implemented")
    }

    override fun update(grupo: Grupos): Grupos? {
        TODO("Not yet implemented")
    }

    override fun delete(id: Int) {
        TODO("Not yet implemented")
    }

    override fun getAll(): List<Grupos>? {
        TODO("Not yet implemented")
    }
}