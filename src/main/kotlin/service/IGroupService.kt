package service

import dataclass.Ctfs
import dataclass.Grupos

interface IGruposService {
    fun crearGrupo(grupo: Grupos): Grupos?
    fun getById(id: Int): Grupos?
    fun update(grupo: Grupos): Grupos?
    fun eliminarGrupo(id: Int)
    fun getAll(): List<Grupos>?
    fun actualizarmejorCtfs(grupo: Grupos, ctfs: List<Ctfs>?):Grupos?
}