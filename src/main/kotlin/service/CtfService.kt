package service

import DAO.IDaoCtf
import dataclass.Ctfs

class CtfService(private val ctfDao:IDaoCtf):ICtfsService {
    override fun create(user: Ctfs): Ctfs? {
        TODO("Not yet implemented")
    }

    override fun getById(id: Int): Ctfs? {
        TODO("Not yet implemented")
    }

    override fun update(Grupo: Ctfs): Ctfs? {
        TODO("Not yet implemented")
    }

    override fun delete(id: Int) {
        TODO("Not yet implemented")
    }

    override fun getAll(): List<Ctfs>? {
        TODO("Not yet implemented")
    }
}