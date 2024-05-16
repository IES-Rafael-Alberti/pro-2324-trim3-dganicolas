package DAO

import dataclass.Ctfs
import dataclass.Grupos

interface IDaoGroup {
    fun insert(grupo: Grupos): Grupos?
    fun getAll(): List<Grupos>?
    fun selectById(id: Int): Grupos?
    fun update(book: Grupos): Grupos?
    fun deleteById(id:Int): Boolean
    fun actualizarPosiciones(grupo: Grupos, ctfs: List<Ctfs>?)
}