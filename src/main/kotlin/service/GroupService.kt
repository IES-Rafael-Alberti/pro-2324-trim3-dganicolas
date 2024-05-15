package service

import DAO.SqlDao.SqlDaoGroup
import dataclass.Grupos

class GroupService(private val groupDao: SqlDaoGroup) :IGruposService {
    override fun create(user: Grupos): Grupos? {
        return groupDao.insert(user)
    }

    override fun getById(id: Int): Grupos? {
        TODO("Not yet implemented")
    }

    override fun update(Grupo: Grupos): Grupos? {
        TODO("Not yet implemented")
    }

    override fun delete(id: Int) {
        TODO("Not yet implemented")
    }

    override fun getAll(): List<Grupos>? {
        TODO("Not yet implemented")
    }
}