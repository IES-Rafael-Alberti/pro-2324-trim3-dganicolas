package comprobadorArgumentos

import dataclassEntity.Ctfs
import dataclassEntity.Grupos

class ComprobadorArgs : IcomprobadorArgs {

    override fun comprobarGrupos(grupo: String): Grupos? {
        return try{if (grupo.contains(";")) {
            val lista = grupo.split(";")
            Grupos(1, lista[1], lista[2].toIntOrNull())
        } else {
            Grupos(1, grupo, null)
        }
        }catch (e: NumberFormatException ){
            null
        }
    }

    override fun comprobarCtfs(ctf: String): Ctfs? {
        val lista = ctf.split(";")
        return try {
            Ctfs(lista[0].toInt(), lista[1].toInt(), lista[2].toInt())
        }catch (e:NumberFormatException){
            null
        }
    }
}