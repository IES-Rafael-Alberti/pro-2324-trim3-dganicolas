package service

import dataclass.Grupos

interface IGruposService {
    fun create(user: Grupos): Grupos?
    fun getById(id: Int): Grupos?
    fun update(Grupo: Grupos): Grupos?
    fun delete(id: Int)
    fun getAll(): List<Grupos>?
}