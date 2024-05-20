package DAO

import dataclass.Ctfs
import dataclass.Grupos

interface IDaoGroup {
    fun crearGrupo(grupo: Grupos): Grupos?
    fun getAll(): List<Grupos>?
    fun selectById(id: Int): Grupos?
    fun update(book: Grupos): Grupos?
    fun eliminarGrupo(id:Int): Boolean
    fun actualizarPosiciones(grupo: Grupos, ctfs: List<Ctfs>?):Grupos?
}