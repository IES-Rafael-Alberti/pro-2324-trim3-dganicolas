package DAO.XmlDao

import DAO.IDAO.IDaoGroup
import dataclassEntity.Ctfs
import dataclassEntity.Grupos

class XmlGruposDao: IDaoGroup {
    override fun crearGrupo(grupo: Grupos): Grupos? {
        TODO("creo un grupo")
    }

    override fun eliminarCtf(grupo: Grupos): Grupos? {
        TODO("pongo el mejorctf a null")
    }

    override fun getAll(): List<Grupos>? {
        TODO("cogo todos los grupos")
    }

    override fun selectById(id: Int): Grupos? {
        TODO("cogo un grupo por su id")
    }

    override fun verificarGrupo(grupo: Grupos): Boolean {
        TODO("verifico un grupo si esta")
    }

    override fun eliminarGrupo(id: Int): Boolean {
        TODO("elimino un grupo")
    }

    override fun actualizarPosiciones(grupo: Grupos, ctfs: List<Ctfs>?): Grupos? {
        TODO("actualizo su posicion")
    }

    override fun mostrarInformacionGrupo(id: Int?): List<Grupos>? {
        TODO("muestro la info del grupo ")
    }
}