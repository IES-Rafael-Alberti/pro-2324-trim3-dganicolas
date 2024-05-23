package DAO.IDAO

import dataclassEntity.Ctfs
import dataclassEntity.Grupos

interface IDaoGroup {
    fun crearGrupo(grupo: Grupos): Grupos?
    fun eliminarCtf(grupo: Grupos):Grupos?
    fun getAll(): List<Grupos>?
    fun selectById(id: Int): Grupos?
    fun verificarGrupo(grupo:Grupos):Boolean
    fun eliminarGrupo(id:Int): Boolean
    fun actualizarPosiciones(grupo: Grupos, ctfs: List<Ctfs>?):Grupos?
    fun mostrarInformacionGrupo(id: Int?=null): List<Grupos>?
}