package comprobadorArgumentos

import dataclassEntity.Ctfs
import dataclassEntity.Grupos

interface IcomprobadorArgs {
    fun comprobarGrupos(grupo: String): Grupos?
    fun comprobarCtfs(ctf: String): Ctfs?
}