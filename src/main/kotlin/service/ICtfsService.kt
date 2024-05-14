package service

import dataclass.Ctfs
import dataclass.Grupos

interface ICtfsService {
    fun create(user: Ctfs): Ctfs?
    fun getById(id: Int): Grupos?
    fun update(Grupo: Ctfs): Ctfs?
    fun delete(id: Int)
    fun getAll(): List<Ctfs>?
}