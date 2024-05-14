package comprobadorArgumentos

import dataclass.Ctfs
import dataclass.Grupos

interface IcomprobadorArgs {
    fun comprobarGrupos(grupo: String): Grupos?
    fun comprobarCtfs(ctf: String): Ctfs?
}