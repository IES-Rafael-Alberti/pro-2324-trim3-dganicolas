package service

import dataclassEntity.Ctfs
import dataclassEntity.Grupos

interface IGruposService {
    fun crearGrupo(grupo: Grupos): Grupos?
    fun getById(id: Int): Grupos?
    fun eliminarCtf(grupo: Grupos):Grupos?
    fun verificargrupo(grupo: Grupos): Boolean
    fun eliminarGrupo(id: Int)
    fun getAll(): List<Grupos>?
    fun actualizarmejorCtfs(grupo: Grupos, ctfs: List<Ctfs>?):Grupos?
    fun mostrarInformacionGrupo(id:Int? = null): List<Grupos>?
}