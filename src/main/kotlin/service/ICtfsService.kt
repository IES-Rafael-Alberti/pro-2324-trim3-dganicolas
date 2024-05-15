package service

import dataclass.Ctfs

interface ICtfsService {
    fun create(user: Ctfs): Ctfs?
    fun getById(id: Int): Ctfs?
    fun update(Grupo: Ctfs): Ctfs?
    fun delete(id: Int)
    fun getAll(): List<Ctfs>?
}