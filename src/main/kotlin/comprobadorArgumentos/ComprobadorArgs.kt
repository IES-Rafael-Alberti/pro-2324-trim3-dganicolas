package comprobadorArgumentos

import dataclass.Ctfs
import dataclass.Grupos

class ComprobadorArgs : IcomprobadorArgs {

    override fun comprobarGrupos(grupo: String): Grupos? {
        return try{if (grupo.contains(";")) {
            val lista = grupo.split(";")
            Grupos(null, lista[1], null)
        } else {
            Grupos(null, grupo, null)
        }
        }catch (e: NumberFormatException ){
            null
        }
    }

    override fun comprobarCtfs(ctf: String): Ctfs? {
        val lista = ctf.split(";")
        return try {
            Ctfs(lista[0].toInt(), lista[0].toInt(), lista[0].toInt())
        }catch (e:NumberFormatException){
            null
        }
    }
}